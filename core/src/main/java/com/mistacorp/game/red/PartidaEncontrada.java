package com.mistacorp.game.red;

import com.mistacorp.game.compartido.TipoMapa;

public class PartidaEncontrada {

    private final String IP;
    private final String NOMBRE_SALA;
    private final int JUGADORES;
    private final int MAXIMO_JUGADORES;
    private final TipoMapa MAPA;
    private final long PING;
    private final long MOMENTO_RECIBIDA;

    public PartidaEncontrada(String ip, String nombreSala, int jugadores, int maximoJugadores, TipoMapa mapa, long ping) {
        this.IP = ip;
        this.NOMBRE_SALA = nombreSala;
        this.JUGADORES = jugadores;
        this.MAXIMO_JUGADORES = maximoJugadores;
        this.MAPA = mapa;
        this.PING = ping;
        this.MOMENTO_RECIBIDA = System.currentTimeMillis();
    }

    public boolean estaLlena() {
        return JUGADORES >= MAXIMO_JUGADORES;
    }

    public boolean esLocal() {
        return IP.startsWith("127.");
    }

    public String obtenerIp() {
        return IP;
    }

    public String obtenerNombreSala() {
        return NOMBRE_SALA;
    }

    public int obtenerJugadores() {
        return JUGADORES;
    }

    public int obtenerMaximoJugadores() {
        return MAXIMO_JUGADORES;
    }

    public TipoMapa obtenerMapa() {
        return MAPA;
    }

    public long obtenerPing() {
        return PING;
    }

    public long obtenerMomentoRecibida() {
        return MOMENTO_RECIBIDA;
    }
}
