package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.entidades.Proyectil;
import com.mistacorp.game.entidades.Tanque;
import com.mistacorp.game.entrada.ManejadorEntradaJugador;
import com.mistacorp.game.interfaz.Hud;
import com.mistacorp.game.mundo.Arena;

public class PantallaJuego extends ScreenAdapter {

    private final KatapumPrincipal juego;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    private final Arena arena = new Arena();
    private final Tanque jugador1;
    private final Tanque jugador2;
    private final Array<Proyectil> proyectiles = new Array<>();

    private final ManejadorEntradaJugador entrada1;
    private final ManejadorEntradaJugador entrada2;
    private final Hud hud;

    private boolean pausado = false;

    public PantallaJuego(KatapumPrincipal juego) {
        this.juego = juego;

        float posicionY = ConfiguracionJuego.ALTO_MUNDO / 2f - ConfiguracionJuego.TAMANO_TANQUE / 2f;
        jugador1 = new Tanque("Jugador 1", ConfiguracionJuego.COLOR_JUGADOR1, 60, posicionY, Tanque.Direccion.DERECHA);
        jugador2 = new Tanque("Jugador 2", ConfiguracionJuego.COLOR_JUGADOR2,
            ConfiguracionJuego.ANCHO_MUNDO - 60 - ConfiguracionJuego.TAMANO_TANQUE, posicionY, Tanque.Direccion.IZQUIERDA);

        entrada1 = new ManejadorEntradaJugador(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        entrada2 = new ManejadorEntradaJugador(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Input.Keys.CONTROL_RIGHT);

        hud = new Hud(juego.obtenerFuente());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(entrada1, entrada2));
    }

    @Override
    public void resize(int ancho, int alto) {
        ventanaGrafica.update(ancho, alto, true);
        hud.redimensionar(ancho, alto);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pausado = !pausado;
        }

        if (!pausado) {
            actualizar(delta);
        }

        ScreenUtils.clear(0, 0, 0, 1);

        ventanaGrafica.apply();
        camara.update();
        juego.obtenerLote().setProjectionMatrix(camara.combined);
        juego.obtenerLote().begin();

        arena.dibujar(juego.obtenerLote(), juego.obtenerPixel());
        jugador1.dibujar(juego.obtenerLote(), juego.obtenerPixel());
        jugador2.dibujar(juego.obtenerLote(), juego.obtenerPixel());
        for (Proyectil proyectil : proyectiles) {
            proyectil.dibujar(juego.obtenerLote(), juego.obtenerPixel());
        }

        juego.obtenerLote().end();

        hud.dibujar(juego.obtenerLote(), jugador1, jugador2, pausado);
    }

    private void actualizar(float delta) {
        manejarJugador(delta, entrada1, jugador1, jugador2);
        manejarJugador(delta, entrada2, jugador2, jugador1);
        actualizarProyectiles(delta);
    }

    private void manejarJugador(float delta, ManejadorEntradaJugador entrada, Tanque tanque, Tanque otro) {
        tanque.intentarMover(entrada.obtenerMovimientoX(), entrada.obtenerMovimientoY(), delta, arena, otro);
        if (entrada.consumirDisparo()) {
            proyectiles.add(new Proyectil(tanque.obtenerPosicionCanon(), tanque.obtenerDireccion(), tanque));
        }
    }

    private void actualizarProyectiles(float delta) {
        for (int i = proyectiles.size - 1; i >= 0; i--) {
            Proyectil proyectil = proyectiles.get(i);
            proyectil.actualizar(delta);

            boolean debeEliminarse = false;

            if (!arena.estaDentroDeLimites(proyectil.obtenerLimites()) || arena.colisionaConParedes(proyectil.obtenerLimites())) {
                debeEliminarse = true;
            } else {
                Tanque objetivo = proyectil.obtenerPropietario() == jugador1 ? jugador2 : jugador1;
                if (proyectil.obtenerLimites().overlaps(objetivo.obtenerLimites())) {
                    objetivo.recibirImpacto();
                    debeEliminarse = true;
                    if (!objetivo.estaVivo()) {
                        juego.setScreen(new PantallaFinPartida(juego, proyectil.obtenerPropietario().obtenerNombre()));
                        return;
                    }
                }
            }

            if (debeEliminarse) {
                proyectiles.removeIndex(i);
            }
        }
    }
}
