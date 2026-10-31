package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class CampoTexto {

    private static final Color COLOR_BORDE = Color.WHITE;
    private static final Color COLOR_RELLENO = new Color(0.16f, 0.16f, 0.22f, 1f);
    private static final float GROSOR_BORDE = 3f;
    private static final float MARGEN_TEXTO = 14f;

    private final Rectangle LIMITES;
    private final StringBuilder TEXTO;
    private final int LARGO_MAXIMO;
    private final TipoCampoTexto TIPO;
    private float tiempoCursor = 0f;

    public CampoTexto(float x, float y, float ancho, float alto, String textoInicial, int largoMaximo, TipoCampoTexto tipo) {
        this.LIMITES = new Rectangle(x, y, ancho, alto);
        this.TEXTO = new StringBuilder(textoInicial);
        this.LARGO_MAXIMO = largoMaximo;
        this.TIPO = tipo;
    }

    public void escribir(char caracter) {
        if (caracter == '\b') {
            if (TEXTO.length() > 0) {
                TEXTO.deleteCharAt(TEXTO.length() - 1);
            }
            return;
        }
        if (TEXTO.length() >= LARGO_MAXIMO || !esPermitido(caracter)) {
            return;
        }
        TEXTO.append(caracter);
    }

    private boolean esPermitido(char caracter) {
        if (TIPO == TipoCampoTexto.DIRECCION_IP) {
            return Character.isDigit(caracter) || caracter == '.';
        }
        return Character.isLetterOrDigit(caracter) || caracter == ' ';
    }

    public void actualizar(float delta) {
        tiempoCursor += delta;
    }

    public void dibujar(SpriteBatch lote, Texture pixel, BitmapFont fuente, boolean editable) {
        lote.setColor(COLOR_BORDE);
        lote.draw(pixel, LIMITES.x, LIMITES.y, LIMITES.width, LIMITES.height);
        lote.setColor(COLOR_RELLENO);
        lote.draw(pixel, LIMITES.x + GROSOR_BORDE, LIMITES.y + GROSOR_BORDE,
            LIMITES.width - GROSOR_BORDE * 2, LIMITES.height - GROSOR_BORDE * 2);
        lote.setColor(Color.WHITE);

        boolean mostrarCursor = editable && ((int) (tiempoCursor * 2)) % 2 == 0;
        String visible = TEXTO.toString() + (mostrarCursor ? "_" : "");
        GlyphLayout layout = new GlyphLayout(fuente, visible);
        fuente.draw(lote, visible, LIMITES.x + MARGEN_TEXTO, LIMITES.y + LIMITES.height / 2f + layout.height / 2f);
    }

    public String obtenerTexto() {
        return TEXTO.toString();
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }
}
