package com.mistacorp.game.mundo;

public class Pared extends Obstaculo {

    private static final int IMPACTOS_PARA_DESTRUIR = 5;

    public Pared(float x, float y, float ancho, float alto) {
        super(x, y, ancho, alto, IMPACTOS_PARA_DESTRUIR);
    }
}
