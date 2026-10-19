package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mistacorp.game.ControlVolumen;

public class SliderVolumen {

    private static final float GROSOR_BORDE = 3f;
    private static final Color COLOR_RELLENO = new Color(0.85f, 0.62f, 0.12f, 1f);
    private static final Color COLOR_FONDO = new Color(0.16f, 0.16f, 0.22f, 1f);

    private final Rectangle LIMITES;

    public SliderVolumen(float x, float y, float ancho, float alto) {
        this.LIMITES = new Rectangle(x, y, ancho, alto);
    }

    public boolean contienePunto(float x, float y) {
        return LIMITES.contains(x, y);
    }

    public void establecerVolumenSegunClick(float mouseX) {
        float proporcion = (mouseX - LIMITES.x) / LIMITES.width;
        ControlVolumen.establecerVolumen(proporcion);
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }

    public void dibujar(SpriteBatch lote, Texture pixel) {
        lote.setColor(Color.WHITE);
        lote.draw(pixel, LIMITES.x, LIMITES.y, LIMITES.width, LIMITES.height);

        lote.setColor(COLOR_FONDO);
        lote.draw(pixel, LIMITES.x + GROSOR_BORDE, LIMITES.y + GROSOR_BORDE,
            LIMITES.width - GROSOR_BORDE * 2, LIMITES.height - GROSOR_BORDE * 2);

        float proporcion = ControlVolumen.estaSilenciado() ? 0f : ControlVolumen.obtenerVolumen();
        float anchoRelleno = (LIMITES.width - GROSOR_BORDE * 2) * proporcion;
        lote.setColor(COLOR_RELLENO);
        lote.draw(pixel, LIMITES.x + GROSOR_BORDE, LIMITES.y + GROSOR_BORDE, anchoRelleno, LIMITES.height - GROSOR_BORDE * 2);

        lote.setColor(Color.WHITE);
    }
}
