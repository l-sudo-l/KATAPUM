package com.mistacorp.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.GameConfig;
import com.mistacorp.game.world.Arena;

/**
 * Tanque controlado por un jugador. Concentra el estado (posición, vidas,
 * dirección hacia la que apunta) y las reglas de movimiento/colisión propias
 * de la mecánica central del juego.
 */
public class Tank {

    /**
     * Dirección hacia la que apunta el tanque. Se usa tanto para orientar
     * el disparo como para saber desde qué punto del cuerpo debe salir el
     * proyectil (el "cañón").
     */
    public enum Direction {
        UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

        public final float dx;
        public final float dy;

        Direction(float dx, float dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private final String name;
    private final Color color;
    private final Vector2 position;
    private final Rectangle bounds;
    private Direction facing;
    private int lives;

    public Tank(String name, Color color, float startX, float startY, Direction facing) {
        this.name = name;
        this.color = color;
        this.position = new Vector2(startX, startY);
        this.bounds = new Rectangle(startX, startY, GameConfig.TANK_SIZE, GameConfig.TANK_SIZE);
        this.facing = facing;
        this.lives = GameConfig.MAX_LIVES;
    }

    /**
     * Intenta mover el tanque según la dirección solicitada por el jugador.
     * La colisión se resuelve eje por eje (primero X, luego Y) en vez de
     * como un único movimiento diagonal: esto permite que el tanque "deslice"
     * contra una pared u otro tanque en vez de quedar trabado por completo
     * al tocar un obstáculo en diagonal.
     */
    public void tryMove(float dx, float dy, float delta, Arena arena, Tank other) {
        if (dx == 0 && dy == 0) {
            return;
        }

        Vector2 direction = new Vector2(dx, dy).nor();
        float distance = GameConfig.TANK_SPEED * delta;

        float newX = position.x + direction.x * distance;
        Rectangle attemptX = new Rectangle(newX, position.y, bounds.width, bounds.height);
        if (isFree(attemptX, arena, other)) {
            position.x = newX;
        }

        float newY = position.y + direction.y * distance;
        Rectangle attemptY = new Rectangle(position.x, newY, bounds.width, bounds.height);
        if (isFree(attemptY, arena, other)) {
            position.y = newY;
        }

        bounds.setPosition(position.x, position.y);
        facing = directionFrom(dx, dy, facing);
    }

    private boolean isFree(Rectangle attempt, Arena arena, Tank other) {
        return arena.isInsideBounds(attempt)
            && !arena.collidesWithWalls(attempt)
            && !attempt.overlaps(other.bounds);
    }

    private static Direction directionFrom(float dx, float dy, Direction current) {
        if (dy > 0) return Direction.UP;
        if (dy < 0) return Direction.DOWN;
        if (dx > 0) return Direction.RIGHT;
        if (dx < 0) return Direction.LEFT;
        return current;
    }

    /** Punto desde donde debe salir un proyectil disparado por este tanque. */
    public Vector2 getMuzzlePosition() {
        float centerX = position.x + bounds.width / 2f;
        float centerY = position.y + bounds.height / 2f;
        float offset = bounds.width / 2f + 4f;
        return new Vector2(centerX + facing.dx * offset, centerY + facing.dy * offset);
    }

    public void hit() {
        lives = Math.max(0, lives - 1);
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public void render(SpriteBatch batch, Texture pixel) {
        batch.setColor(color);
        batch.draw(pixel, position.x, position.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Direction getFacing() {
        return facing;
    }
}
