package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.RecursosSonido;
import com.mistacorp.game.entidades.Efecto;
import com.mistacorp.game.entidades.Proyectil;
import com.mistacorp.game.entidades.Tanque;
import com.mistacorp.game.entrada.ManejadorEntradaJugador;
import com.mistacorp.game.interfaz.Hud;
import com.mistacorp.game.mundo.Arena;

public class PantallaJuego extends ScreenAdapter {

    private final KatapumPrincipal juego;
    private final RecursosGraficos recursos;
    private final RecursosSonido sonidos;

    private final OrthographicCamera camara = new OrthographicCamera();
    private final Viewport ventanaGrafica = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, camara);

    private final Arena arena = new Arena();
    private final Tanque jugador1;
    private final Tanque jugador2;
    private final Array<Proyectil> proyectiles = new Array<>();
    private final Array<Efecto> efectos = new Array<>();

    private final ManejadorEntradaJugador entrada1;
    private final ManejadorEntradaJugador entrada2;
    private final Hud hud;

    private boolean pausado = false;
    private String ganadorPendiente = null;

    public PantallaJuego(KatapumPrincipal juego) {
        this.juego = juego;
        this.recursos = juego.obtenerRecursos();
        this.sonidos = juego.obtenerSonidos();

        float posicionY = ConfiguracionJuego.ALTO_MUNDO / 2f - ConfiguracionJuego.TAMANO_TANQUE / 2f;
        jugador1 = new Tanque("Jugador 1", recursos.obtenerTexturaTanqueVerde(), recursos.obtenerTexturaTanqueVerdeDestruido(),
            recursos.obtenerAnimacionExplosionVerde(), 0f, 60, posicionY, 0f);
        jugador2 = new Tanque("Jugador 2", recursos.obtenerTexturaTanqueAzul(), recursos.obtenerTexturaTanqueAzulDestruido(),
            recursos.obtenerAnimacionExplosionAzul(), 180f,
            ConfiguracionJuego.ANCHO_MUNDO - 60 - ConfiguracionJuego.TAMANO_TANQUE, posicionY, 180f);

        entrada1 = new ManejadorEntradaJugador(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        entrada2 = new ManejadorEntradaJugador(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Input.Keys.CONTROL_RIGHT);

        hud = new Hud(juego.obtenerFuente(), recursos);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(entrada1, entrada2));
        sonidos.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        sonidos.obtenerMusicaJuego().play();
    }

    @Override
    public void hide() {
        sonidos.obtenerMusicaJuego().stop();
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
        manejarControlesDeVolumen();
        sonidos.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());

        if (!pausado) {
            actualizar(delta);
        }

        ScreenUtils.clear(0, 0, 0, 1);

        ventanaGrafica.apply();
        camara.update();
        juego.obtenerLote().setProjectionMatrix(camara.combined);
        juego.obtenerLote().begin();

        arena.dibujar(juego.obtenerLote(), recursos);
        jugador1.dibujar(juego.obtenerLote());
        jugador2.dibujar(juego.obtenerLote());
        for (Proyectil proyectil : proyectiles) {
            proyectil.dibujar(juego.obtenerLote());
        }
        for (Efecto efecto : efectos) {
            efecto.dibujar(juego.obtenerLote());
        }

        juego.obtenerLote().end();

        hud.dibujar(juego.obtenerLote(), jugador1, jugador2, pausado);
    }

    private void actualizar(float delta) {
        manejarJugador(delta, entrada1, jugador1, jugador2);
        manejarJugador(delta, entrada2, jugador2, jugador1);

        jugador1.actualizar(delta);
        jugador2.actualizar(delta);

        actualizarProyectiles(delta);
        actualizarEfectos(delta);
        revisarFinDePartida();
    }

    private void manejarJugador(float delta, ManejadorEntradaJugador entrada, Tanque tanque, Tanque otro) {
        if (!tanque.estaVivo()) {
            return;
        }

        float giro = entrada.obtenerGiro();
        if (giro != 0) {
            tanque.rotar(giro, delta);
        }

        float avance = entrada.obtenerAvance();
        if (avance > 0) {
            tanque.avanzar(delta, arena, otro);
        } else if (avance < 0) {
            tanque.retroceder(delta, arena, otro);
        }

        if (entrada.consumirDisparo()) {
            boolean esJugador1 = tanque == jugador1;
            Animation<TextureRegion> animacionVuelo = esJugador1
                ? recursos.obtenerAnimacionProyectilRojo() : recursos.obtenerAnimacionProyectilAzul();
            Texture flash = esJugador1 ? recursos.obtenerTexturaFlashRojo() : recursos.obtenerTexturaFlashAzul();

            Vector2 posicionCanon = tanque.obtenerPosicionCanon();
            proyectiles.add(new Proyectil(posicionCanon, tanque.obtenerAngulo(), tanque, animacionVuelo));
            efectos.add(new Efecto(flash, posicionCanon, 16, 16));
            sonidos.obtenerSonidoDisparo().play(ControlVolumen.obtenerVolumenEfectivo());
        }
    }

    private void actualizarProyectiles(float delta) {
        for (int i = proyectiles.size - 1; i >= 0; i--) {
            Proyectil proyectil = proyectiles.get(i);
            proyectil.actualizar(delta);

            boolean debeEliminarse = false;

            if (!arena.estaDentroDeLimites(proyectil.obtenerLimites()) || arena.colisionaConParedes(proyectil.obtenerLimites())) {
                debeEliminarse = true;
                agregarEfectoImpacto(proyectil);
            } else {
                Tanque objetivo = proyectil.obtenerPropietario() == jugador1 ? jugador2 : jugador1;
                if (objetivo.estaVivo() && proyectil.obtenerLimites().overlaps(objetivo.obtenerLimites())) {
                    objetivo.recibirImpacto();
                    debeEliminarse = true;
                    agregarEfectoImpacto(proyectil);

                    if (!objetivo.estaVivo()) {
                        objetivo.iniciarExplosion();
                        ganadorPendiente = proyectil.obtenerPropietario().obtenerNombre();
                        sonidos.obtenerSonidoMuerte().play(ControlVolumen.obtenerVolumenEfectivo());
                    }
                }
            }

            if (debeEliminarse) {
                proyectiles.removeIndex(i);
            }
        }
    }

    private void agregarEfectoImpacto(Proyectil proyectil) {
        boolean esJugador1 = proyectil.obtenerPropietario() == jugador1;
        Texture impacto = esJugador1 ? recursos.obtenerTexturaImpactoRojo() : recursos.obtenerTexturaImpactoAzul();
        efectos.add(new Efecto(impacto, proyectil.obtenerCentro(), 16, 16));
        sonidos.obtenerSonidoImpacto().play(ControlVolumen.obtenerVolumenEfectivo());
    }

    private void actualizarEfectos(float delta) {
        for (int i = efectos.size - 1; i >= 0; i--) {
            Efecto efecto = efectos.get(i);
            efecto.actualizar(delta);
            if (efecto.estaFinalizado()) {
                efectos.removeIndex(i);
            }
        }
    }

    private void revisarFinDePartida() {
        if (ganadorPendiente == null) {
            return;
        }
        Tanque tanqueDestruido = jugador1.estaVivo() ? jugador2 : jugador1;
        if (tanqueDestruido.explosionTerminada()) {
            juego.setScreen(new PantallaFinPartida(juego, ganadorPendiente));
        }
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
}
