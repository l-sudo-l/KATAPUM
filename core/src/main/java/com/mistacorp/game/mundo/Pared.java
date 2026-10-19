package com.mistacorp.game.mundo;

import com.badlogic.gdx.math.Rectangle;

public class Pared {

    private final Rectangle LIMITES;

    public Pared(float x, float y, float ancho, float alto) {
        this.LIMITES = new Rectangle(x, y, ancho, alto);
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }
}
