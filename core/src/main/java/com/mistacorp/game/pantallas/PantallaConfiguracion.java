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
import com.mistacorp.game.interfaz.SliderVolumen;

public class PantallaConfiguracion extends PantallaBase {

    private final SliderVolumen SLIDER;
    private final BotonMouse BOTON_VOLVER;

    public PantallaConfiguracion(KatapumPrincipal juego) {
        super(juego);
        SLIDER = new SliderVolumen(CENTRO_X - 150f, 360f, 300f, 26f);
        BOTON_VOLVER = new BotonMouse(CENTRO_X - 150f, 40f, 300f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    JUEGO.setScreen(new PantallaMenu(JUEGO));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        manejarMouse();
        SpriteBatch lote = iniciarDibujo();

        dibujarCentrado("CONFIGURACION", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 60);
        dibujarCentrado("VOLUMEN: " + Math.round(ControlVolumen.obtenerVolumen() * 100) + "%", CENTRO_X, 420f);
        SLIDER.dibujar(lote, JUEGO.obtenerRecursos().obtenerTexturaPixel());

        float x = CENTRO_X - 260f;
        dibujarTexto("CONTROLES", x, 300f);
        dibujarTexto("Partida local, Jugador 1:  W/S avanza y retrocede, A/D gira, ESPACIO dispara", x, 270f);
        dibujarTexto("Partida local, Jugador 2:  flechas, CTRL derecho dispara", x, 245f);
        dibujarTexto("Partida LAN:  cada jugador usa W/S/A/D y ESPACIO en su PC", x, 220f);
        dibujarTexto("ESC: pausa o salir   |   M: silenciar   |   +/-: volumen", x, 195f);

        BOTON_VOLVER.dibujar(lote, JUEGO.obtenerRecursos().obtenerTexturaPixel(), JUEGO.obtenerFuente());
        lote.end();
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);
        if (Gdx.input.justTouched()) {
            if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
                JUEGO.setScreen(new PantallaMenu(JUEGO));
            } else if (SLIDER.contienePunto(mouse.x, mouse.y)) {
                SLIDER.establecerVolumenSegunClick(mouse.x);
            }
        } else if (Gdx.input.isTouched() && SLIDER.contienePunto(mouse.x, mouse.y)) {
            SLIDER.establecerVolumenSegunClick(mouse.x);
        }
    }
}
