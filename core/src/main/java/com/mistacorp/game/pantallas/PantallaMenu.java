package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.interfaz.SliderVolumen;

public class PantallaMenu extends ScreenAdapter {

    private final KatapumPrincipal JUEGO;

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, CAMARA);

    private final BotonMouse BOTON_INICIAR;
    private final SliderVolumen SLIDER;

    public PantallaMenu(KatapumPrincipal juego) {
        this.JUEGO = juego;

        float anchoBoton = 300f;
        float altoBoton = 64f;
        float centroX = ConfiguracionJuego.ANCHO_MUNDO / 2f;

        BOTON_INICIAR = new BotonMouse(centroX - anchoBoton / 2f, 330, anchoBoton, altoBoton, "INICIAR PARTIDA");
        SLIDER = new SliderVolumen(centroX - anchoBoton / 2f, 150, anchoBoton, 26f);
    }

    @Override
    public void show() {
        JUEGO.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        JUEGO.obtenerSonidos().obtenerMusicaMenu().play();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    JUEGO.setScreen(new PantallaJuego(JUEGO));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void hide() {
        JUEGO.obtenerSonidos().obtenerMusicaMenu().stop();
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
    }

    @Override
    public void render(float delta) {
        JUEGO.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        manejarMouse();

        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);

        VENTANA_GRAFICA.apply();
        CAMARA.update();
        JUEGO.obtenerLote().setProjectionMatrix(CAMARA.combined);
        JUEGO.obtenerLote().begin();

        GlyphLayout titulo = new GlyphLayout(JUEGO.obtenerFuente(), "KATAPUM");
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), "KATAPUM",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - titulo.width / 2f, ConfiguracionJuego.ALTO_MUNDO - 70);

        BOTON_INICIAR.dibujar(JUEGO.obtenerLote(), JUEGO.obtenerRecursos().obtenerTexturaPixel(), JUEGO.obtenerFuente());

        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), "VOLUMEN", SLIDER.obtenerLimites().x, SLIDER.obtenerLimites().y + 80);
        SLIDER.dibujar(JUEGO.obtenerLote(), JUEGO.obtenerRecursos().obtenerTexturaPixel());

        dibujarControles();

        JUEGO.obtenerLote().end();
    }

    private void dibujarControles() {
        float x = ConfiguracionJuego.ANCHO_MUNDO / 2f - 220;
        float y = 95;
        float paso = 22;

        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), "CONTROLES", x, y);
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(),
            "Jugador 1:  W avanza · S retrocede · A/D gira · ESPACIO dispara", x, y - paso);
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(),
            "Jugador 2:  Flechas mover/girar · CTRL derecho dispara", x, y - paso * 2);
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(),
            "ESC: pausa   |   M: silenciar   |   +/-: volumen", x, y - paso * 3);
    }

    private void manejarMouse() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        VENTANA_GRAFICA.unproject(mouseMundo);

        BOTON_INICIAR.actualizarResaltado(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            if (BOTON_INICIAR.contienePunto(mouseMundo.x, mouseMundo.y)) {
                JUEGO.setScreen(new PantallaJuego(JUEGO));
            } else if (SLIDER.contienePunto(mouseMundo.x, mouseMundo.y)) {
                SLIDER.establecerVolumenSegunClick(mouseMundo.x);
            }
        } else if (Gdx.input.isTouched() && SLIDER.contienePunto(mouseMundo.x, mouseMundo.y)) {
            SLIDER.establecerVolumenSegunClick(mouseMundo.x);
        }
    }
}
