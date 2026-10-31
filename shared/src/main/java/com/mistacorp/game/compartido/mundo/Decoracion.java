package com.mistacorp.game.compartido.mundo;

public class Decoracion extends Obstaculo {

    private static final int TAMANO = 32;
    private static final int IMPACTOS_PARA_DESTRUIR = 2;

    private final TipoDecoracion TIPO;

    public Decoracion(float x, float y, TipoDecoracion tipo) {
        super(x, y, TAMANO, TAMANO, IMPACTOS_PARA_DESTRUIR);
        this.TIPO = tipo;
    }

    public TipoDecoracion obtenerTipo() {
        return TIPO;
    }
}
