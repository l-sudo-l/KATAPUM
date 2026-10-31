package com.mistacorp.game.red;

import com.badlogic.gdx.Gdx;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class HiloBusqueda extends Thread {

    private final DatagramSocket SOCKET;
    private final OyenteBusqueda OYENTE;
    private volatile boolean activo = true;
    private volatile long ultimaBusqueda = 0;

    public HiloBusqueda(OyenteBusqueda oyente) throws SocketException {
        this.OYENTE = oyente;
        this.SOCKET = new DatagramSocket();
        this.SOCKET.setBroadcast(true);
        this.SOCKET.setSoTimeout(ConfiguracionRed.MILISEGUNDOS_ESPERA_RECEPCION);
        setDaemon(true);
    }

    @Override
    public void run() {
        while (activo) {
            buscarSiCorresponde();
            recibir();
            esperar();
        }
        SOCKET.close();
    }

    private void buscarSiCorresponde() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimaBusqueda < ConfiguracionRed.MILISEGUNDOS_ENTRE_BUSQUEDAS) {
            return;
        }
        ultimaBusqueda = ahora;
        enviarBusqueda(ConfiguracionRed.DIRECCION_BROADCAST);
        enviarBusqueda("127.0.0.1");
    }

    private void enviarBusqueda(String direccion) {
        byte[] datos = Protocolo.armar(TipoMensaje.BUSCAR).getBytes(StandardCharsets.UTF_8);
        try {
            InetAddress destino = InetAddress.getByName(direccion);
            SOCKET.send(new DatagramPacket(datos, datos.length, destino, ConfiguracionRed.PUERTO_SERVIDOR));
        } catch (IOException e) {
            System.out.println("No se pudo buscar en " + direccion + ": " + e.getMessage());
        }
    }

    private void recibir() {
        byte[] bufer = new byte[ConfiguracionRed.TAMANO_BUFER];
        DatagramPacket paquete = new DatagramPacket(bufer, bufer.length);
        try {
            SOCKET.receive(paquete);
            String mensaje = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);
            procesarRespuesta(mensaje, paquete.getAddress().getHostAddress());
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            if (activo) {
                System.out.println("Error al buscar partidas: " + e.getMessage());
            }
        }
    }

    private void procesarRespuesta(String mensaje, String ip) {
        try {
            String[] partes = Protocolo.separar(mensaje);
            if (Protocolo.obtenerTipo(partes) != TipoMensaje.SALA) {
                return;
            }
            long ping = System.currentTimeMillis() - ultimaBusqueda;
            final PartidaEncontrada partida = new PartidaEncontrada(ip, partes[1], Integer.parseInt(partes[2]),
                Integer.parseInt(partes[3]), TipoMapa.valueOf(partes[4]), ping);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (activo) {
                        OYENTE.partidaEncontrada(partida);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Respuesta de busqueda invalida: " + mensaje);
        }
    }

    private void esperar() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            activo = false;
        }
    }

    public void buscarAhora() {
        ultimaBusqueda = 0;
    }

    public void detener() {
        activo = false;
    }
}
