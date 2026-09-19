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

    private final Rectangle limites;

    public SliderVolumen(float x, float y, float ancho, float alto) {
        this.limites = new Rectangle(x, y, ancho, alto);
    }

    public boolean contienePunto(float x, float y) {
        return limites.contains(x, y);
    }

    public void establecerVolumenSegunClick(float mouseX) {
        float proporcion = (mouseX - limites.x) / limites.width;
        ControlVolumen.establecerVolumen(proporcion);
    }

    public Rectangle obtenerLimites() {
        return limites;
    }

    public void dibujar(SpriteBatch lote, Texture pixel) {
        lote.setColor(Color.WHITE);
        lote.draw(pixel, limites.x, limites.y, limites.width, limites.height);

        lote.setColor(COLOR_FONDO);
        lote.draw(pixel, limites.x + GROSOR_BORDE, limites.y + GROSOR_BORDE,
            limites.width - GROSOR_BORDE * 2, limites.height - GROSOR_BORDE * 2);

        float proporcion = ControlVolumen.estaSilenciado() ? 0f : ControlVolumen.obtenerVolumen();
        float anchoRelleno = (limites.width - GROSOR_BORDE * 2) * proporcion;
        lote.setColor(COLOR_RELLENO);
        lote.draw(pixel, limites.x + GROSOR_BORDE, limites.y + GROSOR_BORDE, anchoRelleno, limites.height - GROSOR_BORDE * 2);

        lote.setColor(Color.WHITE);
    }
}
