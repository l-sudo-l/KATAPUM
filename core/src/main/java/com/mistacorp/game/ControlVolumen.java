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

    public static void establecerVolumen(float valor) {
        volumen = Math.max(0f, Math.min(1f, valor));
        silenciado = false;
    }

    public static void alternarSilencio() {
        silenciado = !silenciado;
    }
}
