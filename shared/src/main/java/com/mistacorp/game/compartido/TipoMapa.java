package com.mistacorp.game.compartido;

public enum TipoMapa {
    BOSQUE("Bosque");

    private final String NOMBRE_VISIBLE;

    TipoMapa(String nombreVisible) {
        this.NOMBRE_VISIBLE = nombreVisible;
    }

    public String obtenerNombreVisible() {
        return NOMBRE_VISIBLE;
    }
}
