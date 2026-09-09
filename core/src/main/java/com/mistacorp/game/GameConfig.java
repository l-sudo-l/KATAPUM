package com.mistacorp.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Constantes de configuración centralizadas del juego.
 * <p>
 * Agrupar acá los valores de tamaño, velocidad y color evita "números mágicos"
 * repartidos por las distintas clases y facilita ajustar el balance del
 * prototipo (velocidad de los tanques, cantidad de vidas, etc.) desde un
 * único lugar.
 */
public final class GameConfig {

    private GameConfig() {
        // Clase de solo constantes, no debe instanciarse.
    }

    // --- Mundo / cámara ---
    public static final float WORLD_WIDTH = 960f;
    public static final float WORLD_HEIGHT = 540f;

    // --- Tanques ---
    public static final float TANK_SIZE = 40f;
    public static final float TANK_SPEED = 160f; // píxeles por segundo
    public static final int MAX_LIVES = 3;

    // --- Proyectiles ---
    public static final float PROJECTILE_SIZE = 10f;
    public static final float PROJECTILE_SPEED = 420f; // píxeles por segundo

    // --- Colores de equipo ---
    public static final Color PLAYER1_COLOR = new Color(0.36f, 0.72f, 0.2f, 1f); // verde
    public static final Color PLAYER2_COLOR = new Color(0.25f, 0.5f, 0.85f, 1f); // azul

    // --- Colores de entorno ---
    public static final Color GROUND_COLOR = new Color(0.22f, 0.25f, 0.18f, 1f);
    public static final Color WALL_COLOR = new Color(0.55f, 0.55f, 0.58f, 1f);
    public static final Color PROJECTILE_COLOR = new Color(1f, 0.55f, 0.1f, 1f);
}
