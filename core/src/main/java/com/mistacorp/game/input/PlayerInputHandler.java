package com.mistacorp.game.input;

import com.badlogic.gdx.InputAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase dedicada exclusivamente a procesar las entradas de teclado de un
 * jugador y traducirlas en un estado de control simple (moverX, moverY,
 * disparar). Extiende {@link InputAdapter} para engancharse al sistema de
 * eventos de LibGDX en vez de sondear el teclado a mano.
 * <p>
 * Cada instancia se configura con su propio esquema de teclas, lo que
 * permite que dos jugadores compartan el mismo teclado en el modo local
 * (hotseat): dos {@code PlayerInputHandler} conviven dentro de un mismo
 * {@link com.badlogic.gdx.InputMultiplexer} sin interferir entre sí, porque
 * cada uno sólo reacciona a sus propias teclas.
 */
public class PlayerInputHandler extends InputAdapter {

    private final int moveUpKey;
    private final int moveDownKey;
    private final int moveLeftKey;
    private final int moveRightKey;
    private final int shootKey;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private boolean shootRequested = false;

    public PlayerInputHandler(int moveUpKey, int moveDownKey, int moveLeftKey, int moveRightKey, int shootKey) {
        this.moveUpKey = moveUpKey;
        this.moveDownKey = moveDownKey;
        this.moveLeftKey = moveLeftKey;
        this.moveRightKey = moveRightKey;
        this.shootKey = shootKey;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!isRelevant(keycode)) {
            // Importante: devolver false para que el InputMultiplexer le pase
            // el evento al siguiente handler (el otro jugador).
            return false;
        }
        pressedKeys.add(keycode);
        if (keycode == shootKey) {
            shootRequested = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!isRelevant(keycode)) {
            return false;
        }
        pressedKeys.remove(keycode);
        return true;
    }

    private boolean isRelevant(int keycode) {
        return keycode == moveUpKey || keycode == moveDownKey
            || keycode == moveLeftKey || keycode == moveRightKey
            || keycode == shootKey;
    }

    /** -1, 0 o 1 según si se está presionando izquierda, derecha, ambas o ninguna. */
    public float getMoveX() {
        float x = 0;
        if (pressedKeys.contains(moveRightKey)) x += 1;
        if (pressedKeys.contains(moveLeftKey)) x -= 1;
        return x;
    }

    /** -1, 0 o 1 según si se está presionando abajo, arriba, ambas o ninguna. */
    public float getMoveY() {
        float y = 0;
        if (pressedKeys.contains(moveUpKey)) y += 1;
        if (pressedKeys.contains(moveDownKey)) y -= 1;
        return y;
    }

    /**
     * Devuelve true una única vez por cada pulsación de la tecla de disparo,
     * evitando que mantenerla presionada dispare en cada frame.
     */
    public boolean consumeShoot() {
        if (shootRequested) {
            shootRequested = false;
            return true;
        }
        return false;
    }
}
