package com.mistacorp.game.compartido.mundo;

public class Decoracion extends Obstaculo {

    private static final int TAMANO = 32;
    private static final int IMPACTOS_CAJA_O_BARRIL = 2;

    private final TipoDecoracion TIPO;

    public Decoracion(float x, float y, TipoDecoracion tipo) {
        super(x, y, TAMANO, TAMANO, impactosSegunTipo(tipo));
        this.TIPO = tipo;
    }

    private static int impactosSegunTipo(TipoDecoracion tipo) {
        if (tipo == TipoDecoracion.ARBUSTO) {
            return 0;
        }
        return IMPACTOS_CAJA_O_BARRIL;
    }

    public TipoDecoracion obtenerTipo() {
        return TIPO;
    }
}
