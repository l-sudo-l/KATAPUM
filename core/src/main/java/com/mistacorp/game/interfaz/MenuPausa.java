package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuPausa {

    private final BotonMouse BOTON_CONTINUAR;
    private final BotonMouse BOTON_VOLVER_MENU;
    private final SliderVolumen SLIDER;
    private final float ANCHO_MUNDO;
    private final float ALTO_MUNDO;

    public MenuPausa(float anchoMundo, float altoMundo) {
        this.ANCHO_MUNDO = anchoMundo;
        this.ALTO_MUNDO = altoMundo;

        float anchoBoton = 280f;
        float altoBoton = 56f;
        float centroX = anchoMundo / 2f;
        float centroY = altoMundo / 2f;

        BOTON_CONTINUAR = new BotonMouse(centroX - anchoBoton / 2f, centroY + 30, anchoBoton, altoBoton, "CONTINUAR");
        BOTON_VOLVER_MENU = new BotonMouse(centroX - anchoBoton / 2f, centroY - 40, anchoBoton, altoBoton, "VOLVER AL MENU");
        SLIDER = new SliderVolumen(centroX - anchoBoton / 2f, centroY - 100, anchoBoton, 26f);
    }

    public void actualizarResaltados(float mouseX, float mouseY) {
        BOTON_CONTINUAR.actualizarResaltado(mouseX, mouseY);
        BOTON_VOLVER_MENU.actualizarResaltado(mouseX, mouseY);
    }

    public Accion manejarClick(float mouseX, float mouseY) {
        if (BOTON_CONTINUAR.contienePunto(mouseX, mouseY)) {
            return Accion.CONTINUAR;
        }
        if (BOTON_VOLVER_MENU.contienePunto(mouseX, mouseY)) {
            return Accion.VOLVER_AL_MENU;
        }
        if (SLIDER.contienePunto(mouseX, mouseY)) {
            SLIDER.establecerVolumenSegunClick(mouseX);
        }
        return Accion.NINGUNA;
    }

    public void manejarArrastre(float mouseX, float mouseY) {
        if (SLIDER.contienePunto(mouseX, mouseY)) {
            SLIDER.establecerVolumenSegunClick(mouseX);
        }
    }

    public void dibujar(SpriteBatch lote, Texture pixel, BitmapFont fuente) {
        lote.setColor(0f, 0f, 0f, 0.72f);
        lote.draw(pixel, 0, 0, ANCHO_MUNDO, ALTO_MUNDO);
        lote.setColor(Color.WHITE);

        GlyphLayout titulo = new GlyphLayout(fuente, "PAUSA");
        fuente.draw(lote, "PAUSA", ANCHO_MUNDO / 2f - titulo.width / 2f, ALTO_MUNDO / 2f + 150);

        BOTON_CONTINUAR.dibujar(lote, pixel, fuente);
        BOTON_VOLVER_MENU.dibujar(lote, pixel, fuente);

        GlyphLayout etiquetaVolumen = new GlyphLayout(fuente, "VOLUMEN");
        fuente.draw(lote, "VOLUMEN", SLIDER.obtenerLimites().x, SLIDER.obtenerLimites().y + 50);
        SLIDER.dibujar(lote, pixel);
    }
}
