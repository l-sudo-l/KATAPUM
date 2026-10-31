package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.interfaz.SelectorMapa;

public class PantallaElegirMapa extends PantallaBase {

    private static final float ANCHO_MARCO = 248f;

    private final SelectorMapa SELECTOR_MAPA;
    private final BotonMouse BOTON_JUGAR;
    private final BotonMouse BOTON_VOLVER;

    public PantallaElegirMapa(KatapumPrincipal juego) {
        super(juego);
        SELECTOR_MAPA = new SelectorMapa(CENTRO_X - ANCHO_MARCO / 2f, 260f);
        BOTON_JUGAR = new BotonMouse(CENTRO_X - 320f, 60f, 300f, 60f, "JUGAR");
        BOTON_VOLVER = new BotonMouse(CENTRO_X + 20f, 60f, 300f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    jugar();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    JUEGO.setScreen(new PantallaMenu(JUEGO));
                    return true;
                }
                if (keycode == Input.Keys.LEFT) {
                    SELECTOR_MAPA.cambiar(-1);
                    return true;
                }
                if (keycode == Input.Keys.RIGHT) {
                    SELECTOR_MAPA.cambiar(1);
                    return true;
                }
                return false;
            }
        });
    }

    private void jugar() {
        JUEGO.setScreen(new PantallaJuego(JUEGO, SELECTOR_MAPA.obtenerMapa()));
    }

    @Override
    public void render(float delta) {
        manejarMouse();
        SpriteBatch lote = iniciarDibujo();
        Texture pixel = JUEGO.obtenerRecursos().obtenerTexturaPixel();

        dibujarCentrado("PARTIDA LOCAL", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 50);
        dibujarCentrado("Elegi el mapa", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 90);
        SELECTOR_MAPA.dibujar(lote, JUEGO.obtenerRecursos(), JUEGO.obtenerFuente());
        BOTON_JUGAR.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_VOLVER.dibujar(lote, pixel, JUEGO.obtenerFuente());
        dibujarCentrado("Flechas: cambiar mapa  |  ENTER: jugar  |  ESC: volver", CENTRO_X, 150f);
        lote.end();
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_JUGAR.actualizarResaltado(mouse.x, mouse.y);
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);
        if (!Gdx.input.justTouched()) {
            return;
        }
        if (BOTON_JUGAR.contienePunto(mouse.x, mouse.y)) {
            jugar();
        } else if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaMenu(JUEGO));
        } else {
            SELECTOR_MAPA.manejarClick(mouse.x, mouse.y);
        }
    }
}
