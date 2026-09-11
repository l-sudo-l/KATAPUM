package com.mistacorp.game.entrada;

import com.badlogic.gdx.InputAdapter;

import java.util.HashSet;
import java.util.Set;

public class ManejadorEntradaJugador extends InputAdapter {

    private final int teclaArriba;
    private final int teclaAbajo;
    private final int teclaIzquierda;
    private final int teclaDerecha;
    private final int teclaDisparo;

    private final Set<Integer> teclasPresionadas = new HashSet<>();
    private boolean disparoSolicitado = false;

    public ManejadorEntradaJugador(int teclaArriba, int teclaAbajo, int teclaIzquierda, int teclaDerecha, int teclaDisparo) {
        this.teclaArriba = teclaArriba;
        this.teclaAbajo = teclaAbajo;
        this.teclaIzquierda = teclaIzquierda;
        this.teclaDerecha = teclaDerecha;
        this.teclaDisparo = teclaDisparo;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!esRelevante(keycode)) {
            return false;
        }
        teclasPresionadas.add(keycode);
        if (keycode == teclaDisparo) {
            disparoSolicitado = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!esRelevante(keycode)) {
            return false;
        }
        teclasPresionadas.remove(keycode);
        return true;
    }

    private boolean esRelevante(int keycode) {
        return keycode == teclaArriba || keycode == teclaAbajo
            || keycode == teclaIzquierda || keycode == teclaDerecha
            || keycode == teclaDisparo;
    }

    public float obtenerMovimientoX() {
        float x = 0;
        if (teclasPresionadas.contains(teclaDerecha)) x += 1;
        if (teclasPresionadas.contains(teclaIzquierda)) x -= 1;
        return x;
    }

    public float obtenerMovimientoY() {
        float y = 0;
        if (teclasPresionadas.contains(teclaArriba)) y += 1;
        if (teclasPresionadas.contains(teclaAbajo)) y -= 1;
        return y;
    }

    public boolean consumirDisparo() {
        if (disparoSolicitado) {
            disparoSolicitado = false;
            return true;
        }
        return false;
    }
}
