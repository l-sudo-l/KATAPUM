package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.entrada.EntradaJugador;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.MotivoFin;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class HiloServidor extends Thread {

    private static final int MAXIMO_JUGADORES = 2;
    private static final int REPETICIONES_AVISO_FIN = 3;

    private final DatagramSocket SOCKET;
    private final String NOMBRE_SALA;
    private final TipoMapa MAPA;
    private final ClienteConectado[] CLIENTES = new ClienteConectado[MAXIMO_JUGADORES];
    private volatile boolean activo = true;
    private volatile HiloPartida partida = null;

    public HiloServidor(int puerto, String nombreSala, TipoMapa mapa) throws SocketException {
        this.NOMBRE_SALA = Protocolo.limpiarNombreSala(nombreSala);
        this.MAPA = mapa;
        this.SOCKET = new DatagramSocket(puerto);
        this.SOCKET.setSoTimeout(ConfiguracionRed.MILISEGUNDOS_ESPERA_RECEPCION);
    }

    @Override
    public void run() {
        while (activo) {
            recibir();
            revisarDesconexiones();
            esperar();
        }
        terminarPartida();
        SOCKET.close();
    }

    private void recibir() {
        byte[] bufer = new byte[ConfiguracionRed.TAMANO_BUFER];
        DatagramPacket paquete = new DatagramPacket(bufer, bufer.length);
        try {
            SOCKET.receive(paquete);
            String mensaje = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);
            procesarMensaje(mensaje, paquete.getAddress(), paquete.getPort());
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            if (activo) {
                System.out.println("Error al recibir: " + e.getMessage());
            }
        }
    }

    private void procesarMensaje(String mensaje, InetAddress direccion, int puerto) {
        try {
            String[] partes = Protocolo.separar(mensaje);
            TipoMensaje tipo = Protocolo.obtenerTipo(partes);
            if (tipo == null) {
                return;
            }
            ClienteConectado cliente = buscarCliente(direccion, puerto);
            if (cliente != null) {
                cliente.registrarActividad();
            }
            switch (tipo) {
                case CONECTAR:
                    registrarCliente(cliente, direccion, puerto);
                    break;
                case INPUT:
                    if (cliente != null) {
                        aplicarEntrada(cliente, partes);
                    }
                    break;
                case DESCONECTAR:
                    if (cliente != null) {
                        liberarLugar(cliente, "se desconecto");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Mensaje invalido ignorado: " + mensaje);
        }
    }

    private void aplicarEntrada(ClienteConectado cliente, String[] partes) {
        HiloPartida actual = partida;
        if (actual == null) {
            return;
        }
        int secuencia = Integer.parseInt(partes[1]);
        if (!cliente.esEntradaNueva(secuencia)) {
            return;
        }
        float giro = Integer.parseInt(partes[2]) - 1;
        float avance = Integer.parseInt(partes[3]) - 1;
        int contadorDisparos = Integer.parseInt(partes[4]);
        EntradaJugador entrada = actual.obtenerEntrada(cliente.obtenerNumeroJugador());
        entrada.establecerMovimiento(giro, avance);
        if (cliente.hayDisparoNuevo(contadorDisparos)) {
            entrada.solicitarDisparo();
        }
    }

    private synchronized void registrarCliente(ClienteConectado existente, InetAddress direccion, int puerto) {
        if (existente != null) {
            enviarAsignacion(existente);
            if (estaCompleto()) {
                enviar(Protocolo.armar(TipoMensaje.EMPEZAR), direccion, puerto);
            }
            return;
        }
        int lugarLibre = buscarLugarLibre();
        if (lugarLibre < 0) {
            enviar(Protocolo.armar(TipoMensaje.SALA_LLENA), direccion, puerto);
            return;
        }
        ClienteConectado nuevo = new ClienteConectado(direccion, puerto, lugarLibre + 1);
        CLIENTES[lugarLibre] = nuevo;
        System.out.println("Jugador " + nuevo.obtenerNumeroJugador() + " conectado desde " + direccion.getHostAddress() + ":" + puerto);
        enviarAsignacion(nuevo);
        if (estaCompleto()) {
            enviarATodos(Protocolo.armar(TipoMensaje.EMPEZAR));
            iniciarPartida();
            System.out.println("Sala completa, empieza la partida");
        }
    }

    private void iniciarPartida() {
        partida = new HiloPartida(this);
        partida.start();
    }

    private void avisarVictoriaPorDesconexion() {
        if (partida == null || partida.estaTerminada()) {
            return;
        }
        for (ClienteConectado restante : CLIENTES) {
            if (restante != null) {
                String mensaje = Protocolo.armar(TipoMensaje.FIN, String.valueOf(restante.obtenerNumeroJugador()),
                    String.valueOf(MotivoFin.RIVAL_DESCONECTADO.ordinal()));
                for (int i = 0; i < REPETICIONES_AVISO_FIN; i++) {
                    enviar(mensaje, restante.obtenerDireccion(), restante.obtenerPuerto());
                }
                System.out.println("Gana el jugador " + restante.obtenerNumeroJugador() + " por desconexion del rival");
            }
        }
    }

    private void terminarPartida() {
        if (partida != null) {
            partida.detener();
            partida = null;
        }
    }

    private synchronized void revisarDesconexiones() {
        long ahora = System.currentTimeMillis();
        for (ClienteConectado cliente : CLIENTES) {
            if (cliente != null && ahora - cliente.obtenerUltimoMensaje() > ConfiguracionRed.MILISEGUNDOS_DESCONEXION) {
                liberarLugar(cliente, "dejo de responder");
            }
        }
    }

    private synchronized void liberarLugar(ClienteConectado cliente, String motivo) {
        CLIENTES[cliente.obtenerNumeroJugador() - 1] = null;
        avisarVictoriaPorDesconexion();
        terminarPartida();
        System.out.println("Jugador " + cliente.obtenerNumeroJugador() + " " + motivo + ", se libera su lugar");
    }

    private void enviarAsignacion(ClienteConectado cliente) {
        String mensaje = Protocolo.armar(TipoMensaje.ASIGNAR, String.valueOf(cliente.obtenerNumeroJugador()),
            NOMBRE_SALA, MAPA.name());
        enviar(mensaje, cliente.obtenerDireccion(), cliente.obtenerPuerto());
    }

    private synchronized ClienteConectado buscarCliente(InetAddress direccion, int puerto) {
        for (ClienteConectado cliente : CLIENTES) {
            if (cliente != null && cliente.esMismo(direccion, puerto)) {
                return cliente;
            }
        }
        return null;
    }

    private int buscarLugarLibre() {
        for (int i = 0; i < CLIENTES.length; i++) {
            if (CLIENTES[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public boolean estaCompleto() {
        return buscarLugarLibre() < 0;
    }

    public synchronized void enviarATodos(String mensaje) {
        for (ClienteConectado cliente : CLIENTES) {
            if (cliente != null) {
                enviar(mensaje, cliente.obtenerDireccion(), cliente.obtenerPuerto());
            }
        }
    }

    private synchronized void enviar(String mensaje, InetAddress direccion, int puerto) {
        byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, direccion, puerto);
        try {
            SOCKET.send(paquete);
        } catch (IOException e) {
            System.out.println("Error al enviar: " + e.getMessage());
        }
    }

    private void esperar() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            activo = false;
        }
    }

    public String obtenerNombreSala() {
        return NOMBRE_SALA;
    }

    public void detener() {
        activo = false;
    }
}
