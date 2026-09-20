package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuPausa {

    public enum Accion {
        NINGUNA, CONTINUAR, VOLVER_AL_MENU
    }

    private final BotonMouse botonContinuar;
    private final BotonMouse botonVolverMenu;
    private final SliderVolumen slider;
    private final float anchoMundo;
    private final float altoMundo;

    public MenuPausa(float anchoMundo, float altoMundo) {
        this.anchoMundo = anchoMundo;
        this.altoMundo = altoMundo;

        float anchoBoton = 280f;
        float altoBoton = 56f;
        float centroX = anchoMundo / 2f;
        float centroY = altoMundo / 2f;

        botonContinuar = new BotonMouse(centroX - anchoBoton / 2f, centroY + 30, anchoBoton, altoBoton, "CONTINUAR");
        botonVolverMenu = new BotonMouse(centroX - anchoBoton / 2f, centroY - 40, anchoBoton, altoBoton, "VOLVER AL MENU");
        slider = new SliderVolumen(centroX - anchoBoton / 2f, centroY - 100, anchoBoton, 26f);
    }

    public void actualizarResaltados(float mouseX, float mouseY) {
        botonContinuar.actualizarResaltado(mouseX, mouseY);
        botonVolverMenu.actualizarResaltado(mouseX, mouseY);
    }

    public Accion manejarClick(float mouseX, float mouseY) {
        if (botonContinuar.contienePunto(mouseX, mouseY)) {
            return Accion.CONTINUAR;
        }
        if (botonVolverMenu.contienePunto(mouseX, mouseY)) {
            return Accion.VOLVER_AL_MENU;
        }
        if (slider.contienePunto(mouseX, mouseY)) {
            slider.establecerVolumenSegunClick(mouseX);
        }
        return Accion.NINGUNA;
    }

    public void manejarArrastre(float mouseX, float mouseY) {
        if (slider.contienePunto(mouseX, mouseY)) {
            slider.establecerVolumenSegunClick(mouseX);
        }
    }

    public void dibujar(SpriteBatch lote, Texture pixel, BitmapFont fuente) {
        lote.setColor(0f, 0f, 0f, 0.72f);
        lote.draw(pixel, 0, 0, anchoMundo, altoMundo);
        lote.setColor(Color.WHITE);

        GlyphLayout titulo = new GlyphLayout(fuente, "PAUSA");
        fuente.draw(lote, "PAUSA", anchoMundo / 2f - titulo.width / 2f, altoMundo / 2f + 150);

        botonContinuar.dibujar(lote, pixel, fuente);
        botonVolverMenu.dibujar(lote, pixel, fuente);

        GlyphLayout etiquetaVolumen = new GlyphLayout(fuente, "VOLUMEN");
        fuente.draw(lote, "VOLUMEN", slider.obtenerLimites().x, slider.obtenerLimites().y + 50);
        slider.dibujar(lote, pixel);
    }
}
