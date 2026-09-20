package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.entidades.Tanque;

public class Hud {

    private static final float TAMANO_CORAZON = 26f;
    private static final float SEPARACION_CORAZON = 30f;
    private static final float MARGEN_NOMBRE_CORAZONES = 14f;
    private static final float ESCALA_NOMBRE = 1.8f;
    private static final float ESCALA_NORMAL = 1.2f;
    private static final float GROSOR_CONTORNO = 2f;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new ScreenViewport(camara);
    private final BitmapFont fuente;
    private final Texture texturaCorazon;
    private final GlyphLayout layout = new GlyphLayout();

    public Hud(BitmapFont fuente, RecursosGraficos recursos) {
        this.fuente = fuente;
        this.texturaCorazon = recursos.obtenerTexturaCorazon();
    }

    public void redimensionar(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
    }

    public void dibujar(SpriteBatch lote, Tanque jugador1, Tanque jugador2) {
        ventanaGrafica.apply();
        lote.setProjectionMatrix(camara.combined);
        lote.begin();

        float topeSuperior = ventanaGrafica.getWorldHeight() - 30;
        dibujarPanelJugador(lote, jugador1, 16, topeSuperior, true);
        dibujarPanelJugador(lote, jugador2, ventanaGrafica.getWorldWidth() - 16, topeSuperior, false);

        fuente.getData().setScale(ESCALA_NORMAL);
        fuente.draw(lote, textoVolumen(), 16, 24);

        lote.end();
    }

    private String textoVolumen() {
        if (ControlVolumen.estaSilenciado()) {
            return "Volumen: silenciado (M)";
        }
        int porcentaje = Math.round(ControlVolumen.obtenerVolumen() * 100);
        return "Volumen: " + porcentaje + "% (+/- , M)";
    }

    private void dibujarPanelJugador(SpriteBatch lote, Tanque tanque, float xBorde, float y, boolean nombreALaIzquierda) {
        fuente.getData().setScale(ESCALA_NOMBRE);
        layout.setText(fuente, tanque.obtenerNombre());
        float anchoNombre = layout.width;
        float altoNombre = layout.height;

        float anchoBloqueCorazones = ConfiguracionJuego.VIDAS_MAXIMAS * SEPARACION_CORAZON;
        float xNombre;
        float xInicioCorazones;

        if (nombreALaIzquierda) {
            xNombre = xBorde;
            xInicioCorazones = xNombre + anchoNombre + MARGEN_NOMBRE_CORAZONES;
        } else {
            xNombre = xBorde - anchoNombre;
            xInicioCorazones = xNombre - MARGEN_NOMBRE_CORAZONES - anchoBloqueCorazones;
        }

        dibujarTextoConContorno(lote, tanque.obtenerNombre(), xNombre, y);

        float yCorazon = y - altoNombre / 2f - TAMANO_CORAZON / 2f;
        for (int i = 0; i < tanque.obtenerVidas(); i++) {
            float xCorazon = nombreALaIzquierda
                ? xInicioCorazones + i * SEPARACION_CORAZON
                : xInicioCorazones + anchoBloqueCorazones - (i + 1) * SEPARACION_CORAZON;
            lote.draw(texturaCorazon, xCorazon, yCorazon, TAMANO_CORAZON, TAMANO_CORAZON);
        }
    }

    private void dibujarTextoConContorno(SpriteBatch lote, String texto, float x, float y) {
        fuente.setColor(Color.BLACK);
        fuente.draw(lote, texto, x - GROSOR_CONTORNO, y);
        fuente.draw(lote, texto, x + GROSOR_CONTORNO, y);
        fuente.draw(lote, texto, x, y - GROSOR_CONTORNO);
        fuente.draw(lote, texto, x, y + GROSOR_CONTORNO);
        fuente.draw(lote, texto, x - GROSOR_CONTORNO, y - GROSOR_CONTORNO);
        fuente.draw(lote, texto, x + GROSOR_CONTORNO, y - GROSOR_CONTORNO);
        fuente.draw(lote, texto, x - GROSOR_CONTORNO, y + GROSOR_CONTORNO);
        fuente.draw(lote, texto, x + GROSOR_CONTORNO, y + GROSOR_CONTORNO);

        fuente.setColor(Color.WHITE);
        fuente.draw(lote, texto, x, y);
    }
}
