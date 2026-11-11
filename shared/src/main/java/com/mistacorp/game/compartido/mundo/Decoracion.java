package com.mistacorp.game.compartido.mundo;

import com.badlogic.gdx.math.Rectangle;

public class Decoracion extends Obstaculo {

    private static final int TAMANO = 32;
    private static final int IMPACTOS_PARA_DESTRUIR = 2;
    private static final float MARGEN_COLISION = 6f;

    private final TipoDecoracion TIPO;
    private final Rectangle LIMITES_COLISION;

    public Decoracion(float x, float y, TipoDecoracion tipo) {
        super(x, y, TAMANO, TAMANO, IMPACTOS_PARA_DESTRUIR);
        this.TIPO = tipo;
        this.LIMITES_COLISION = new Rectangle(x + MARGEN_COLISION, y + MARGEN_COLISION,
            TAMANO - MARGEN_COLISION * 2f, TAMANO - MARGEN_COLISION * 2f);
    }

    @Override
    public Rectangle obtenerLimitesColision() {
        return LIMITES_COLISION;
    }

    public TipoDecoracion obtenerTipo() {
        return TIPO;
    }
}
