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

    private final Rectangle limites;
    private final String texto;
    private boolean resaltado = false;

    public BotonMouse(float x, float y, float ancho, float alto, String texto) {
        this.limites = new Rectangle(x, y, ancho, alto);
        this.texto = texto;
    }

    public boolean contienePunto(float x, float y) {
        return limites.contains(x, y);
    }

    public void actualizarResaltado(float mouseX, float mouseY) {
        resaltado = contienePunto(mouseX, mouseY);
    }

    public Rectangle obtenerLimites() {
        return limites;
    }

    public void dibujar(SpriteBatch lote, Texture pixel, BitmapFont fuente) {
        lote.setColor(COLOR_BORDE);
        lote.draw(pixel, limites.x, limites.y, limites.width, limites.height);

        lote.setColor(resaltado ? COLOR_RELLENO_RESALTADO : COLOR_RELLENO);
        lote.draw(pixel, limites.x + GROSOR_BORDE, limites.y + GROSOR_BORDE,
            limites.width - GROSOR_BORDE * 2, limites.height - GROSOR_BORDE * 2);
        lote.setColor(Color.WHITE);

        GlyphLayout layout = new GlyphLayout(fuente, texto);
        float tx = limites.x + limites.width / 2f - layout.width / 2f;
        float ty = limites.y + limites.height / 2f + layout.height / 2f;
        fuente.draw(lote, texto, tx, ty);
    }
}
