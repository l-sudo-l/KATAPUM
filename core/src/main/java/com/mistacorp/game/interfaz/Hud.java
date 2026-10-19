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

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new ScreenViewport(CAMARA);
    private final BitmapFont FUENTE;
    private final Texture TEXTURA_CORAZON;
    private final GlyphLayout LAYOUT = new GlyphLayout();

    public Hud(BitmapFont fuente, RecursosGraficos recursos) {
        this.FUENTE = fuente;
        this.TEXTURA_CORAZON = recursos.obtenerTexturaCorazon();
    }

    public void redimensionar(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
    }

    public void dibujar(SpriteBatch lote, Tanque jugador1, Tanque jugador2) {
        VENTANA_GRAFICA.apply();
        lote.setProjectionMatrix(CAMARA.combined);
        lote.begin();

        float topeSuperior = VENTANA_GRAFICA.getWorldHeight() - 30;
        dibujarPanelJugador(lote, jugador1, 16, topeSuperior, true);
        dibujarPanelJugador(lote, jugador2, VENTANA_GRAFICA.getWorldWidth() - 16, topeSuperior, false);

        FUENTE.getData().setScale(ESCALA_NORMAL);
        FUENTE.draw(lote, textoVolumen(), 16, 24);

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
        FUENTE.getData().setScale(ESCALA_NOMBRE);
        LAYOUT.setText(FUENTE, tanque.obtenerNombre());
        float anchoNombre = LAYOUT.width;
        float altoNombre = LAYOUT.height;

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
            lote.draw(TEXTURA_CORAZON, xCorazon, yCorazon, TAMANO_CORAZON, TAMANO_CORAZON);
        }
    }

    private void dibujarTextoConContorno(SpriteBatch lote, String texto, float x, float y) {
        FUENTE.setColor(Color.BLACK);
        FUENTE.draw(lote, texto, x - GROSOR_CONTORNO, y);
        FUENTE.draw(lote, texto, x + GROSOR_CONTORNO, y);
        FUENTE.draw(lote, texto, x, y - GROSOR_CONTORNO);
        FUENTE.draw(lote, texto, x, y + GROSOR_CONTORNO);
        FUENTE.draw(lote, texto, x - GROSOR_CONTORNO, y - GROSOR_CONTORNO);
        FUENTE.draw(lote, texto, x + GROSOR_CONTORNO, y - GROSOR_CONTORNO);
        FUENTE.draw(lote, texto, x - GROSOR_CONTORNO, y + GROSOR_CONTORNO);
        FUENTE.draw(lote, texto, x + GROSOR_CONTORNO, y + GROSOR_CONTORNO);

        FUENTE.setColor(Color.WHITE);
        FUENTE.draw(lote, texto, x, y);
    }
}
