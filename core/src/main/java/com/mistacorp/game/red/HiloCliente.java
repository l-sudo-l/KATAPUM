package com.mistacorp.game.red;

import com.badlogic.gdx.Gdx;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class HiloCliente extends Thread {

    private final DatagramSocket SOCKET;
    private final InetAddress DIRECCION_SERVIDOR;
    private final int PUERTO_SERVIDOR;
    private volatile OyenteRed oyente;
    private volatile boolean activo = true;
    private volatile boolean partidaIniciada = false;
    private long ultimoIntentoConexion = 0;

    public HiloCliente(String ipServidor, int puertoServidor, OyenteRed oyente) throws UnknownHostException, SocketException {
        this.DIRECCION_SERVIDOR = InetAddress.getByName(ipServidor);
        this.PUERTO_SERVIDOR = puertoServidor;
        this.oyente = oyente;
        this.SOCKET = new DatagramSocket();
        this.SOCKET.setSoTimeout(ConfiguracionRed.MILISEGUNDOS_ESPERA_RECEPCION);
        setDaemon(true);
    }

    @Override
    public void run() {
        while (activo) {
            reintentarConexion();
            recibir();
            esperar();
        }
        SOCKET.close();
    }

    private void reintentarConexion() {
        if (partidaIniciada) {
            return;
        }
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoIntentoConexion >= ConfiguracionRed.MILISEGUNDOS_REINTENTO_CONEXION) {
            ultimoIntentoConexion = ahora;
            enviar(Protocolo.armar(TipoMensaje.CONECTAR));
        }
    }

    private void recibir() {
        byte[] bufer = new byte[ConfiguracionRed.TAMANO_BUFER];
        DatagramPacket paquete = new DatagramPacket(bufer, bufer.length);
        try {
            SOCKET.receive(paquete);
            String mensaje = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);
            avisarAlJuego(mensaje);
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            if (activo) {
                System.out.println("Error al recibir: " + e.getMessage());
            }
        }
    }

    private void avisarAlJuego(String mensaje) {
        final String[] partes = Protocolo.separar(mensaje);
        final TipoMensaje tipo = Protocolo.obtenerTipo(partes);
        if (tipo == null) {
            return;
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                OyenteRed destino = oyente;
                if (destino == null) {
                    return;
                }
                try {
                    destino.mensajeRecibido(tipo, partes);
                } catch (Exception e) {
                    System.out.println("Mensaje invalido ignorado: " + tipo);
                }
            }
        });
    }

    public synchronized void enviar(String mensaje) {
        byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, DIRECCION_SERVIDOR, PUERTO_SERVIDOR);
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

    public void establecerOyente(OyenteRed oyente) {
        this.oyente = oyente;
    }

    public void marcarPartidaIniciada() {
        partidaIniciada = true;
    }

    public void detener() {
        enviar(Protocolo.armar(TipoMensaje.DESCONECTAR));
        oyente = null;
        activo = false;
    }
}
