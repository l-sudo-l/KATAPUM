package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;

public abstract class PantallaBase extends ScreenAdapter {

    protected static final float CENTRO_X = ConfiguracionPartida.ANCHO_MUNDO / 2f;
    private static final Color COLOR_PANEL = new Color(0.16f, 0.16f, 0.22f, 1f);
    private static final float GROSOR_BORDE = 3f;

    protected final KatapumPrincipal JUEGO;
    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO, CAMARA);

    protected PantallaBase(KatapumPrincipal juego) {
        this.JUEGO = juego;
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
    }

    protected Vector2 obtenerMouseMundo() {
        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        VENTANA_GRAFICA.unproject(mouse);
        return mouse;
    }

    protected SpriteBatch iniciarDibujo() {
        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);
        VENTANA_GRAFICA.apply();
        CAMARA.update();
        SpriteBatch lote = JUEGO.obtenerLote();
        lote.setProjectionMatrix(CAMARA.combined);
        lote.begin();
        return lote;
    }

    protected void dibujarCentrado(String texto, float x, float y) {
        BitmapFont fuente = JUEGO.obtenerFuente();
        GlyphLayout layout = new GlyphLayout(fuente, texto);
        fuente.draw(JUEGO.obtenerLote(), texto, x - layout.width / 2f, y);
    }

    protected void dibujarTexto(String texto, float x, float y) {
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), texto, x, y);
    }

    protected void dibujarPanel(Rectangle limites) {
        SpriteBatch lote = JUEGO.obtenerLote();
        Texture pixel = JUEGO.obtenerRecursos().obtenerTexturaPixel();
        lote.setColor(Color.WHITE);
        lote.draw(pixel, limites.x, limites.y, limites.width, limites.height);
        lote.setColor(COLOR_PANEL);
        lote.draw(pixel, limites.x + GROSOR_BORDE, limites.y + GROSOR_BORDE,
            limites.width - GROSOR_BORDE * 2, limites.height - GROSOR_BORDE * 2);
        lote.setColor(Color.WHITE);
    }
}
