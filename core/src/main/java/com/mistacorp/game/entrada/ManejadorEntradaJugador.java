package com.mistacorp.game.entrada;

import com.badlogic.gdx.InputAdapter;
import com.mistacorp.game.compartido.entrada.EntradaJugador;

import java.util.HashSet;
import java.util.Set;

public class ManejadorEntradaJugador extends InputAdapter {

    private final int TECLA_ARRIBA;
    private final int TECLA_ABAJO;
    private final int TECLA_IZQUIERDA;
    private final int TECLA_DERECHA;
    private final int TECLA_DISPARO;

    private final Set<Integer> TECLAS_PRESIONADAS = new HashSet<>();
    private final EntradaJugador ENTRADA = new EntradaJugador();

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
            ENTRADA.solicitarDisparo();
        }
        actualizarMovimiento();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!esRelevante(keycode)) {
            return false;
        }
        TECLAS_PRESIONADAS.remove(keycode);
        actualizarMovimiento();
        return true;
    }

    private boolean esRelevante(int keycode) {
        return keycode == TECLA_ARRIBA || keycode == TECLA_ABAJO
            || keycode == TECLA_IZQUIERDA || keycode == TECLA_DERECHA
            || keycode == TECLA_DISPARO;
    }

    private void actualizarMovimiento() {
        float giro = 0;
        if (TECLAS_PRESIONADAS.contains(TECLA_IZQUIERDA)) giro += 1;
        if (TECLAS_PRESIONADAS.contains(TECLA_DERECHA)) giro -= 1;

        float avance = 0;
        if (TECLAS_PRESIONADAS.contains(TECLA_ARRIBA)) avance += 1;
        if (TECLAS_PRESIONADAS.contains(TECLA_ABAJO)) avance -= 1;

        ENTRADA.establecerMovimiento(giro, avance);
    }

    public EntradaJugador obtenerEntrada() {
        return ENTRADA;
    }
}
