package com.mistacorp.game.compartido.entrada;

public class EntradaJugador {

    private float giro = 0f;
    private float avance = 0f;
    private boolean disparoSolicitado = false;

    public synchronized void establecerMovimiento(float giro, float avance) {
        this.giro = giro;
        this.avance = avance;
    }

    public synchronized void solicitarDisparo() {
        disparoSolicitado = true;
    }

    public synchronized boolean consumirDisparo() {
        if (disparoSolicitado) {
            disparoSolicitado = false;
            return true;
        }
        return false;
    }

    public synchronized float obtenerGiro() {
        return giro;
    }

    public synchronized float obtenerAvance() {
        return avance;
    }
}
