package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.interfaz.BotonMouse;

public class PantallaMenu extends PantallaBase {

    private static final float ANCHO_BOTON = 300f;
    private static final float ALTO_BOTON = 64f;

    private final BotonMouse BOTON_LOCAL;
    private final BotonMouse BOTON_LAN;
    private final BotonMouse BOTON_CONFIGURACION;

    public PantallaMenu(KatapumPrincipal juego) {
        super(juego);
        float x = CENTRO_X - ANCHO_BOTON / 2f;
        BOTON_LOCAL = new BotonMouse(x, 330, ANCHO_BOTON, ALTO_BOTON, "PARTIDA LOCAL");
        BOTON_LAN = new BotonMouse(x, 240, ANCHO_BOTON, ALTO_BOTON, "PARTIDA LAN");
        BOTON_CONFIGURACION = new BotonMouse(x, 150, ANCHO_BOTON, ALTO_BOTON, "CONFIGURACION");
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
    public void render(float delta) {
        JUEGO.obtenerSonidos().obtenerMusicaMenu().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        manejarMouse();

        SpriteBatch lote = iniciarDibujo();
        dibujarCentrado("KATAPUM", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 70);
        BOTON_LOCAL.dibujar(lote, JUEGO.obtenerRecursos().obtenerTexturaPixel(), JUEGO.obtenerFuente());
        BOTON_LAN.dibujar(lote, JUEGO.obtenerRecursos().obtenerTexturaPixel(), JUEGO.obtenerFuente());
        BOTON_CONFIGURACION.dibujar(lote, JUEGO.obtenerRecursos().obtenerTexturaPixel(), JUEGO.obtenerFuente());
        dibujarCentrado("ENTER: partida local", CENTRO_X, 90);
        lote.end();
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_LOCAL.actualizarResaltado(mouse.x, mouse.y);
        BOTON_LAN.actualizarResaltado(mouse.x, mouse.y);
        BOTON_CONFIGURACION.actualizarResaltado(mouse.x, mouse.y);

        if (!Gdx.input.justTouched()) {
            return;
        }
        if (BOTON_LOCAL.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaJuego(JUEGO));
        } else if (BOTON_LAN.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaConectar(JUEGO));
        } else if (BOTON_CONFIGURACION.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaConfiguracion(JUEGO));
        }
    }
}
