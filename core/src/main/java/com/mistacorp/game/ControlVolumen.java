package com.mistacorp.game;

public final class ControlVolumen {

    private static float volumen = 0.6f;
    private static boolean silenciado = false;

    private ControlVolumen() {
    }

    public static float obtenerVolumenEfectivo() {
        return silenciado ? 0f : volumen;
    }

    public static float obtenerVolumen() {
        return volumen;
    }

    public static boolean estaSilenciado() {
        return silenciado;
    }

    public static void subirVolumen() {
        volumen = Math.min(1f, volumen + 0.1f);
    }

    public static void bajarVolumen() {
        volumen = Math.max(0f, volumen - 0.1f);
    }

    public static void alternarSilencio() {
        silenciado = !silenciado;
    }
}
