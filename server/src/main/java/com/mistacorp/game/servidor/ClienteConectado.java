package com.mistacorp.game.servidor;

import java.net.InetAddress;

public class ClienteConectado {

    private final InetAddress DIRECCION;
    private final int PUERTO;
    private final int NUMERO_JUGADOR;
    private long ultimoMensaje;
    private int disparosProcesados = 0;

    public ClienteConectado(InetAddress direccion, int puerto, int numeroJugador) {
        this.DIRECCION = direccion;
        this.PUERTO = puerto;
        this.NUMERO_JUGADOR = numeroJugador;
        this.ultimoMensaje = System.currentTimeMillis();
    }

    public boolean esMismo(InetAddress direccion, int puerto) {
        return DIRECCION.equals(direccion) && PUERTO == puerto;
    }

    public void registrarActividad() {
        ultimoMensaje = System.currentTimeMillis();
    }

    public boolean hayDisparoNuevo(int contadorDisparos) {
        if (contadorDisparos > disparosProcesados) {
            disparosProcesados = contadorDisparos;
            return true;
        }
        return false;
    }

    public long obtenerUltimoMensaje() {
        return ultimoMensaje;
    }

    public InetAddress obtenerDireccion() {
        return DIRECCION;
    }

    public int obtenerPuerto() {
        return PUERTO;
    }

    public int obtenerNumeroJugador() {
        return NUMERO_JUGADOR;
    }
}
