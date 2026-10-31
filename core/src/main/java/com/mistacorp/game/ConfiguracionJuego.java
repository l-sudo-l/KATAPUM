package com.mistacorp.game;

import com.badlogic.gdx.graphics.Color;

public final class ConfiguracionJuego {

    private ConfiguracionJuego() {
    }

    public static final float RETRASO_FIN_PARTIDA = 1.5f;

    public static final Color COLOR_JUGADOR1 = new Color(0.36f, 0.72f, 0.2f, 1f);
    public static final Color COLOR_JUGADOR2 = new Color(0.25f, 0.5f, 0.85f, 1f);

    public static final Color COLOR_SUELO = new Color(0.22f, 0.25f, 0.18f, 1f);
    public static final Color COLOR_PARED = new Color(0.55f, 0.55f, 0.58f, 1f);
    public static final Color COLOR_PROYECTIL = new Color(1f, 0.55f, 0.1f, 1f);
}
