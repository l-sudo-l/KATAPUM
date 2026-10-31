package com.mistacorp.game.compartido;

public class EventoPartida {

    private final TipoEvento TIPO;
    private final float X;
    private final float Y;
    private final int NUMERO_JUGADOR;

    public EventoPartida(TipoEvento tipo, float x, float y, int numeroJugador) {
        this.TIPO = tipo;
        this.X = x;
        this.Y = y;
        this.NUMERO_JUGADOR = numeroJugador;
    }

    public TipoEvento obtenerTipo() {
        return TIPO;
    }

    public float obtenerX() {
        return X;
    }

    public float obtenerY() {
        return Y;
    }

    public int obtenerNumeroJugador() {
        return NUMERO_JUGADOR;
    }
}
