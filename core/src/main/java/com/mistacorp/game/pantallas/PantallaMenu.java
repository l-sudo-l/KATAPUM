package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;

public class PantallaMenu extends ScreenAdapter {

    private final KatapumPrincipal juego;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    public PantallaMenu(KatapumPrincipal juego) {
        this.juego = juego;
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
        manejarControlesDeVolumen();
        juego.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());

        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);

        ventanaGrafica.apply();
        camara.update();
        juego.obtenerLote().setProjectionMatrix(camara.combined);
        juego.obtenerLote().begin();

        juego.obtenerFuente().draw(juego.obtenerLote(), "KATAPUM",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - 60, ConfiguracionJuego.ALTO_MUNDO - 80);
        juego.obtenerFuente().draw(juego.obtenerLote(), "Presiona ENTER para jugar",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - 130, ConfiguracionJuego.ALTO_MUNDO / 2f + 60);
        juego.obtenerFuente().draw(juego.obtenerLote(), "Jugador 1: W A S D + ESPACIO",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - 150, ConfiguracionJuego.ALTO_MUNDO / 2f + 10);
        juego.obtenerFuente().draw(juego.obtenerLote(), "Jugador 2: Flechas + CTRL derecho",
            ConfiguracionJuego.ANCHO_MUNDO / 2f - 150, ConfiguracionJuego.ALTO_MUNDO / 2f - 20);
        juego.obtenerFuente().draw(juego.obtenerLote(), textoVolumen(),
            ConfiguracionJuego.ANCHO_MUNDO / 2f - 150, ConfiguracionJuego.ALTO_MUNDO / 2f - 60);

        juego.obtenerLote().end();
    }

    private void manejarControlesDeVolumen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            ControlVolumen.alternarSilencio();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            ControlVolumen.subirVolumen();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            ControlVolumen.bajarVolumen();
        }
    }

    private String textoVolumen() {
        if (ControlVolumen.estaSilenciado()) {
            return "Volumen: silenciado (M para activar)";
        }
        int porcentaje = Math.round(ControlVolumen.obtenerVolumen() * 100);
        return "Volumen: " + porcentaje + "%   (+/- ajustar, M silenciar)";
    }
}
