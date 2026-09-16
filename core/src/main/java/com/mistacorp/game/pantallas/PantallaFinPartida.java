package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;

public class PantallaFinPartida extends ScreenAdapter {

    private final KatapumPrincipal juego;
    private final String nombreGanador;
    private final Texture texturaBanner;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    public PantallaFinPartida(KatapumPrincipal juego, String nombreGanador) {
        this.juego = juego;
        this.nombreGanador = nombreGanador;

        RecursosGraficos recursos = juego.obtenerRecursos();
        this.texturaBanner = "Jugador 1".equals(nombreGanador)
            ? recursos.obtenerTexturaGanadorJugador1()
            : recursos.obtenerTexturaGanadorJugador2();
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
        float anchoBoton = 220f;
        float altoBoton = 60f;
        float separacion = 40f;
        float xBotones = ConfiguracionJuego.ANCHO_MUNDO / 2f - (anchoBoton * 2 + separacion) / 2f;
        float yBotones = 180f;

        juego.obtenerLote().draw(recursos.obtenerTexturaBotonVolverJugar(), xBotones, yBotones, anchoBoton, altoBoton);
        juego.obtenerLote().draw(recursos.obtenerTexturaBotonVolverMenu(),
            xBotones + anchoBoton + separacion, yBotones, anchoBoton, altoBoton);

        juego.obtenerFuente().draw(juego.obtenerLote(), "ENTER", xBotones + 80, yBotones - 12);
        juego.obtenerFuente().draw(juego.obtenerLote(), "ESC", xBotones + anchoBoton + separacion + 90, yBotones - 12);

        juego.obtenerLote().end();
    }
}
