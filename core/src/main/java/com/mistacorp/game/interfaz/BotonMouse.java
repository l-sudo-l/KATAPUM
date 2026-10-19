package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BotonMouse {

    private static final Color COLOR_BORDE = Color.WHITE;
    private static final Color COLOR_RELLENO = new Color(0.16f, 0.16f, 0.22f, 1f);
    private static final Color COLOR_RELLENO_RESALTADO = new Color(0.85f, 0.62f, 0.12f, 1f);
    private static final float GROSOR_BORDE = 3f;

    private final Rectangle LIMITES;
    private final String TEXTO;
    private boolean resaltado = false;

    public BotonMouse(float x, float y, float ancho, float alto, String texto) {
        this.LIMITES = new Rectangle(x, y, ancho, alto);
        this.TEXTO = texto;
    }

    public boolean contienePunto(float x, float y) {
        return LIMITES.contains(x, y);
    }

    public void actualizarResaltado(float mouseX, float mouseY) {
        resaltado = contienePunto(mouseX, mouseY);
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }

    public void dibujar(SpriteBatch lote, Texture pixel, BitmapFont fuente) {
        lote.setColor(COLOR_BORDE);
        lote.draw(pixel, LIMITES.x, LIMITES.y, LIMITES.width, LIMITES.height);

        lote.setColor(resaltado ? COLOR_RELLENO_RESALTADO : COLOR_RELLENO);
        lote.draw(pixel, LIMITES.x + GROSOR_BORDE, LIMITES.y + GROSOR_BORDE,
            LIMITES.width - GROSOR_BORDE * 2, LIMITES.height - GROSOR_BORDE * 2);
        lote.setColor(Color.WHITE);

        GlyphLayout layout = new GlyphLayout(fuente, TEXTO);
        float tx = LIMITES.x + LIMITES.width / 2f - layout.width / 2f;
        float ty = LIMITES.y + LIMITES.height / 2f + layout.height / 2f;
        fuente.draw(lote, TEXTO, tx, ty);
    }
}
