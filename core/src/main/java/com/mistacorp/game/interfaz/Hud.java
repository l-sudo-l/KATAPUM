package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.entidades.Tanque;

public class Hud {

    private static final float TAMANO_CORAZON = 18f;
    private static final float SEPARACION_CORAZON = 22f;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new ScreenViewport(camara);
    private final BitmapFont fuente;
    private final Texture texturaCorazon;

    public Hud(BitmapFont fuente, RecursosGraficos recursos) {
        this.fuente = fuente;
        this.texturaCorazon = recursos.obtenerTexturaCorazon();
    }

    public void redimensionar(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
    }

    public void dibujar(SpriteBatch lote, Tanque jugador1, Tanque jugador2, boolean pausado) {
        ventanaGrafica.apply();
        lote.setProjectionMatrix(camara.combined);
        lote.begin();

        dibujarPanelJugador(lote, jugador1, 16);
        dibujarPanelJugador(lote, jugador2, ventanaGrafica.getWorldWidth() - 220);

        if (pausado) {
            fuente.draw(lote, "PAUSA - ESC para continuar",
                ventanaGrafica.getWorldWidth() / 2f - 110, ventanaGrafica.getWorldHeight() / 2f);
        }

        lote.end();
    }

    private void dibujarPanelJugador(SpriteBatch lote, Tanque tanque, float x) {
        float topeSuperior = ventanaGrafica.getWorldHeight() - 16;
        fuente.draw(lote, tanque.obtenerNombre(), x, topeSuperior);

        float y = topeSuperior - 30;
        for (int i = 0; i < tanque.obtenerVidas(); i++) {
            lote.draw(texturaCorazon, x + i * SEPARACION_CORAZON, y, TAMANO_CORAZON, TAMANO_CORAZON);
        }
    }
}
