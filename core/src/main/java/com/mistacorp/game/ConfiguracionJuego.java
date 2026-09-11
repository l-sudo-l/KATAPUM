package com.mistacorp.game;

import com.badlogic.gdx.graphics.Color;

public final class ConfiguracionJuego {

    private ConfiguracionJuego() {
    }

    public static final float ANCHO_MUNDO = 960f;
    public static final float ALTO_MUNDO = 540f;

    public static final float TAMANO_TANQUE = 40f;
    public static final float VELOCIDAD_TANQUE = 160f;
    public static final int VIDAS_MAXIMAS = 3;

    public static final float TAMANO_PROYECTIL = 10f;
    public static final float VELOCIDAD_PROYECTIL = 420f;

    public static final Color COLOR_JUGADOR1 = new Color(0.36f, 0.72f, 0.2f, 1f);
    public static final Color COLOR_JUGADOR2 = new Color(0.25f, 0.5f, 0.85f, 1f);

    public static final Color COLOR_SUELO = new Color(0.22f, 0.25f, 0.18f, 1f);
    public static final Color COLOR_PARED = new Color(0.55f, 0.55f, 0.58f, 1f);
    public static final Color COLOR_PROYECTIL = new Color(1f, 0.55f, 0.1f, 1f);
}
