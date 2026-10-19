package com.mistacorp.game.entrada;

import com.badlogic.gdx.InputAdapter;

import java.util.HashSet;
import java.util.Set;

public class ManejadorEntradaJugador extends InputAdapter {

    private final int TECLA_ARRIBA;
    private final int TECLA_ABAJO;
    private final int TECLA_IZQUIERDA;
    private final int TECLA_DERECHA;
    private final int TECLA_DISPARO;

    private final Set<Integer> TECLAS_PRESIONADAS = new HashSet<>();
    private boolean disparoSolicitado = false;

    public ManejadorEntradaJugador(int teclaArriba, int teclaAbajo, int teclaIzquierda, int teclaDerecha, int teclaDisparo) {
        this.TECLA_ARRIBA = teclaArriba;
        this.TECLA_ABAJO = teclaAbajo;
        this.TECLA_IZQUIERDA = teclaIzquierda;
        this.TECLA_DERECHA = teclaDerecha;
        this.TECLA_DISPARO = teclaDisparo;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!esRelevante(keycode)) {
            return false;
        }
        TECLAS_PRESIONADAS.add(keycode);
        if (keycode == TECLA_DISPARO) {
            disparoSolicitado = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!esRelevante(keycode)) {
            return false;
        }
        TECLAS_PRESIONADAS.remove(keycode);
        return true;
    }

    private boolean esRelevante(int keycode) {
        return keycode == TECLA_ARRIBA || keycode == TECLA_ABAJO
            || keycode == TECLA_IZQUIERDA || keycode == TECLA_DERECHA
            || keycode == TECLA_DISPARO;
    }

    public float obtenerGiro() {
        float giro = 0;
        if (TECLAS_PRESIONADAS.contains(TECLA_IZQUIERDA)) giro += 1;
        if (TECLAS_PRESIONADAS.contains(TECLA_DERECHA)) giro -= 1;
        return giro;
    }

    public float obtenerAvance() {
        float avance = 0;
        if (TECLAS_PRESIONADAS.contains(TECLA_ARRIBA)) avance += 1;
        if (TECLAS_PRESIONADAS.contains(TECLA_ABAJO)) avance -= 1;
        return avance;
    }

    public boolean consumirDisparo() {
        if (disparoSolicitado) {
            disparoSolicitado = false;
            return true;
        }
        return false;
    }
}
