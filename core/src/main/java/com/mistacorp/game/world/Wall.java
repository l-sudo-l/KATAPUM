package com.mistacorp.game.world;

import com.badlogic.gdx.math.Rectangle;

/**
 * Obstáculo fijo e indestructible del mapa. Bloquea el movimiento de los
 * tanques y la trayectoria de los proyectiles, funcionando como cobertura
 * táctica dentro de la arena.
 */
public class Wall {

    private final Rectangle bounds;

    public Wall(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
