package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.entidades.Tanque;

public class Hud {

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new ScreenViewport(camara);
    private final BitmapFont fuente;

    public Hud(BitmapFont fuente) {
        this.fuente = fuente;
    }

    public void redimensionar(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
    }

    public void dibujar(SpriteBatch lote, Tanque jugador1, Tanque jugador2, boolean pausado) {
        ventanaGrafica.apply();
        lote.setProjectionMatrix(camara.combined);
        lote.begin();

        fuente.draw(lote, etiquetaVidas(jugador1), 16, ventanaGrafica.getWorldHeight() - 16);
        fuente.draw(lote, etiquetaVidas(jugador2), ventanaGrafica.getWorldWidth() - 220, ventanaGrafica.getWorldHeight() - 16);

        if (pausado) {
            fuente.draw(lote, "PAUSA - ESC para continuar",
                ventanaGrafica.getWorldWidth() / 2f - 110, ventanaGrafica.getWorldHeight() / 2f);
        }

        lote.end();
    }

    private String etiquetaVidas(Tanque tanque) {
        StringBuilder corazones = new StringBuilder();
        for (int i = 0; i < tanque.obtenerVidas(); i++) {
            corazones.append("<3 ");
        }
        return tanque.obtenerNombre() + "  " + corazones.toString().trim();
    }
}
