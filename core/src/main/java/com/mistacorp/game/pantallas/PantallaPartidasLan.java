package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.interfaz.BotonMouse;

public class PantallaPartidasLan extends PantallaBase {

    private final Rectangle PANEL_LISTA;
    private final BotonMouse BOTON_CREAR;
    private final BotonMouse BOTON_IP;
    private final BotonMouse BOTON_VOLVER;

    public PantallaPartidasLan(KatapumPrincipal juego) {
        super(juego);
        PANEL_LISTA = new Rectangle(60f, 150f, 840f, 300f);
        BOTON_CREAR = new BotonMouse(60f, 50f, 260f, 60f, "CREAR PARTIDA");
        BOTON_IP = new BotonMouse(350f, 50f, 260f, 60f, "CONECTAR POR IP");
        BOTON_VOLVER = new BotonMouse(640f, 50f, 260f, 60f, "VOLVER");
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
        Texture pixel = JUEGO.obtenerRecursos().obtenerTexturaPixel();

        dibujarCentrado("PARTIDAS EN LA RED LOCAL", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 50);
        dibujarPanel(PANEL_LISTA);
        dibujarCentrado("Crea una partida o conectate por IP", CENTRO_X, PANEL_LISTA.y + PANEL_LISTA.height / 2f);
        BOTON_CREAR.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_IP.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_VOLVER.dibujar(lote, pixel, JUEGO.obtenerFuente());
        lote.end();
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_CREAR.actualizarResaltado(mouse.x, mouse.y);
        BOTON_IP.actualizarResaltado(mouse.x, mouse.y);
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);

        if (!Gdx.input.justTouched()) {
            return;
        }
        if (BOTON_CREAR.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaCrearPartida(JUEGO));
        } else if (BOTON_IP.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaConectar(JUEGO));
        } else if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaMenu(JUEGO));
        }
    }
}
