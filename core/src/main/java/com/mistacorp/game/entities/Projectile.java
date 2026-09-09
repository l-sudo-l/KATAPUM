package com.mistacorp.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.GameConfig;

/**
 * Proyectil disparado por un tanque. Se mueve en línea recta a velocidad
 * constante hasta impactar contra una pared, salir del mapa, o chocar con
 * el tanque contrario.
 */
public class Projectile {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle bounds;
    private final Tank owner;

    public Projectile(Vector2 spawnPosition, Tank.Direction direction, Tank owner) {
        this.position = new Vector2(
            spawnPosition.x - GameConfig.PROJECTILE_SIZE / 2f,
            spawnPosition.y - GameConfig.PROJECTILE_SIZE / 2f
        );
        this.velocity = new Vector2(direction.dx, direction.dy).scl(GameConfig.PROJECTILE_SPEED);
        this.bounds = new Rectangle(position.x, position.y, GameConfig.PROJECTILE_SIZE, GameConfig.PROJECTILE_SIZE);
        this.owner = owner;
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
        bounds.setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch, Texture pixel) {
        batch.setColor(GameConfig.PROJECTILE_COLOR);
        batch.draw(pixel, position.x, position.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Tank getOwner() {
        return owner;
    }
}
