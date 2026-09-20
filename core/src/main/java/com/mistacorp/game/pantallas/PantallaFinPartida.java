package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;

public class PantallaFinPartida extends ScreenAdapter {

    private static final Color COLOR_RESALTADO = new Color(1f, 1f, 1f, 0.25f);

    private final KatapumPrincipal juego;
    private final Texture texturaBanner;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    private final Rectangle limitesBotonJugar;
    private final Rectangle limitesBotonMenu;
    private boolean botonJugarResaltado = false;
    private boolean botonMenuResaltado = false;

    public PantallaFinPartida(KatapumPrincipal juego, String nombreGanador) {
        this.juego = juego;

        RecursosGraficos recursos = juego.obtenerRecursos();
        this.texturaBanner = "Jugador 1".equals(nombreGanador)
            ? recursos.obtenerTexturaGanadorJugador1()
            : recursos.obtenerTexturaGanadorJugador2();

        float anchoBoton = 220f;
        float altoBoton = 60f;
        float separacion = 40f;
        float xBotones = ConfiguracionJuego.ANCHO_MUNDO / 2f - (anchoBoton * 2 + separacion) / 2f;
        float yBotones = 180f;

        limitesBotonJugar = new Rectangle(xBotones, yBotones, anchoBoton, altoBoton);
        limitesBotonMenu = new Rectangle(xBotones + anchoBoton + separacion, yBotones, anchoBoton, altoBoton);
    }

    @Override
    public void show() {
        juego.obtenerSonidos().obtenerSonidoGanar().play(ControlVolumen.obtenerVolumenEfectivo());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    juego.setScreen(new PantallaJuego(juego));
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    juego.setScreen(new PantallaMenu(juego));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
    }

    @Override
    public void render(float delta) {
        manejarMouse();

        ScreenUtils.clear(0.05f, 0.05f, 0.08f, 1f);

        ventanaGrafica.apply();
        camara.update();
        juego.obtenerLote().setProjectionMatrix(camara.combined);
        juego.obtenerLote().begin();

        float anchoBanner = 420f;
        float altoBanner = 110f;
        float xBanner = ConfiguracionJuego.ANCHO_MUNDO / 2f - anchoBanner / 2f;
        float yBanner = 300f;
        juego.obtenerLote().draw(texturaBanner, xBanner, yBanner, anchoBanner, altoBanner);

        RecursosGraficos recursos = juego.obtenerRecursos();
        Texture pixel = recursos.obtenerTexturaPixel();

        juego.obtenerLote().draw(recursos.obtenerTexturaBotonVolverJugar(),
            limitesBotonJugar.x, limitesBotonJugar.y, limitesBotonJugar.width, limitesBotonJugar.height);
        juego.obtenerLote().draw(recursos.obtenerTexturaBotonVolverMenu(),
            limitesBotonMenu.x, limitesBotonMenu.y, limitesBotonMenu.width, limitesBotonMenu.height);

        if (botonJugarResaltado) {
            juego.obtenerLote().setColor(COLOR_RESALTADO);
            juego.obtenerLote().draw(pixel, limitesBotonJugar.x, limitesBotonJugar.y, limitesBotonJugar.width, limitesBotonJugar.height);
            juego.obtenerLote().setColor(Color.WHITE);
        }
        if (botonMenuResaltado) {
            juego.obtenerLote().setColor(COLOR_RESALTADO);
            juego.obtenerLote().draw(pixel, limitesBotonMenu.x, limitesBotonMenu.y, limitesBotonMenu.width, limitesBotonMenu.height);
            juego.obtenerLote().setColor(Color.WHITE);
        }

        juego.obtenerFuente().draw(juego.obtenerLote(), "ENTER", limitesBotonJugar.x + 80, limitesBotonJugar.y - 12);
        juego.obtenerFuente().draw(juego.obtenerLote(), "ESC", limitesBotonMenu.x + 90, limitesBotonMenu.y - 12);

        juego.obtenerLote().end();
    }

    private void manejarMouse() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        ventanaGrafica.unproject(mouseMundo);

        botonJugarResaltado = limitesBotonJugar.contains(mouseMundo.x, mouseMundo.y);
        botonMenuResaltado = limitesBotonMenu.contains(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            if (botonJugarResaltado) {
                juego.setScreen(new PantallaJuego(juego));
            } else if (botonMenuResaltado) {
                juego.setScreen(new PantallaMenu(juego));
            }
        }
    }
}
