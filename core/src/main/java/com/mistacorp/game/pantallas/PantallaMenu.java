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

    private final KatapumPrincipal juego;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    private final BotonMouse botonIniciar;
    private final SliderVolumen slider;

    public PantallaMenu(KatapumPrincipal juego) {
        this.juego = juego;

        float anchoBoton = 300f;
        float altoBoton = 64f;
        float centroX = ConfiguracionJuego.ANCHO_MUNDO / 2f;

        botonIniciar = new BotonMouse(centroX - anchoBoton / 2f, 330, anchoBoton, altoBoton, "INICIAR PARTIDA");
        slider = new SliderVolumen(centroX - anchoBoton / 2f, 250, anchoBoton, 26f);
    }

    @Override
    public void show() {
        juego.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        juego.obtenerSonidos().obtenerMusicaMenu().play();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    juego.setScreen(new PantallaJuego(juego));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void hide() {
        juego.obtenerSonidos().obtenerMusicaMenu().stop();
    }

    @Override
    public void resize(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
    }

    @Override
    public void render(float delta) {
        juego.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        manejarMouse();

        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);

        ventanaGrafica.apply();
        camara.update();
        juego.obtenerLote().setProjectionMatrix(camara.combined);
        juego.obtenerLote().begin();

        GlyphLayout titulo = new GlyphLayout(juego.obtenerFuente(), "KATAPUM");
        juego.obtenerFuente().draw(juego.obtenerLote(), "KATAPUM",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - titulo.width / 2f, ConfiguracionJuego.ALTO_MUNDO - 70);

        botonIniciar.dibujar(juego.obtenerLote(), juego.obtenerRecursos().obtenerTexturaPixel(), juego.obtenerFuente());

        juego.obtenerFuente().draw(juego.obtenerLote(), "VOLUMEN", slider.obtenerLimites().x, slider.obtenerLimites().y + 34);
        slider.dibujar(juego.obtenerLote(), juego.obtenerRecursos().obtenerTexturaPixel());

        dibujarControles();

        juego.obtenerLote().end();
    }

    private void dibujarControles() {
        float x = ConfiguracionJuego.ANCHO_MUNDO / 2f - 220;
        float y = 170;
        float paso = 26;

        juego.obtenerFuente().draw(juego.obtenerLote(), "CONTROLES", x, y);
        juego.obtenerFuente().draw(juego.obtenerLote(),
            "Jugador 1:  W avanza · S retrocede · A/D gira · ESPACIO dispara", x, y - paso);
        juego.obtenerFuente().draw(juego.obtenerLote(),
            "Jugador 2:  Flechas mover/girar · CTRL derecho dispara", x, y - paso * 2);
        juego.obtenerFuente().draw(juego.obtenerLote(),
            "ESC: pausa   |   M: silenciar   |   +/-: volumen", x, y - paso * 3);
    }

    private void manejarMouse() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        ventanaGrafica.unproject(mouseMundo);

        botonIniciar.actualizarResaltado(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            if (botonIniciar.contienePunto(mouseMundo.x, mouseMundo.y)) {
                juego.setScreen(new PantallaJuego(juego));
            } else if (slider.contienePunto(mouseMundo.x, mouseMundo.y)) {
                slider.establecerVolumenSegunClick(mouseMundo.x);
            }
        } else if (Gdx.input.isTouched() && slider.contienePunto(mouseMundo.x, mouseMundo.y)) {
            slider.establecerVolumenSegunClick(mouseMundo.x);
        }
    }
}
