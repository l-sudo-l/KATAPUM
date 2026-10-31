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
import com.mistacorp.game.interfaz.Accion;
import com.mistacorp.game.interfaz.Hud;
import com.mistacorp.game.interfaz.MenuPausa;
import com.mistacorp.game.mundo.Arena;

public class PantallaJuego extends ScreenAdapter {

    private final KatapumPrincipal JUEGO;
    private final RecursosGraficos RECURSOS;
    private final RecursosSonido SONIDOS;

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO, CAMARA);

    private final Arena ARENA = new Arena();
    private final Tanque JUGADOR1;
    private final Tanque JUGADOR2;
    private final Array<Proyectil> PROYECTILES = new Array<>();
    private final Array<Efecto> EFECTOS = new Array<>();

    private final ManejadorEntradaJugador ENTRADA1;
    private final ManejadorEntradaJugador ENTRADA2;
    private final Hud HUD;
    private final MenuPausa MENU_PAUSA;

    private boolean pausado = false;
    private String ganadorPendiente = null;
    private float tiempoEsperaFinPartida = 0f;

    public PantallaJuego(KatapumPrincipal juego) {
        this.JUEGO = juego;
        this.RECURSOS = juego.obtenerRecursos();
        this.SONIDOS = juego.obtenerSonidos();

        float posicionY = ConfiguracionJuego.ALTO_MUNDO / 2f - ConfiguracionJuego.TAMANO_TANQUE / 2f;
        JUGADOR1 = new Tanque("Jugador 1", RECURSOS.obtenerTexturaTanqueVerde(), RECURSOS.obtenerTexturaTanqueVerdeDestruido(),
            RECURSOS.obtenerAnimacionExplosionVerde(), 0f, 60, posicionY, 0f);
        JUGADOR2 = new Tanque("Jugador 2", RECURSOS.obtenerTexturaTanqueAzul(), RECURSOS.obtenerTexturaTanqueAzulDestruido(),
            RECURSOS.obtenerAnimacionExplosionAzul(), 0f,
            ConfiguracionJuego.ANCHO_MUNDO - 60 - ConfiguracionJuego.TAMANO_TANQUE, posicionY, 180f);

        ENTRADA1 = new ManejadorEntradaJugador(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        ENTRADA2 = new ManejadorEntradaJugador(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Input.Keys.CONTROL_RIGHT);

        HUD = new Hud(juego.obtenerFuente(), RECURSOS);
        MENU_PAUSA = new MenuPausa(ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(ENTRADA1, ENTRADA2));
        SONIDOS.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        SONIDOS.obtenerMusicaJuego().play();
    }

    @Override
    public void hide() {
        SONIDOS.obtenerMusicaJuego().stop();
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
        HUD.redimensionar(ancho, alto);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pausado = !pausado;
        }
        manejarControlesDeVolumen();
        SONIDOS.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());

        if (!pausado) {
            actualizar(delta);
        } else {
            manejarMousePausa();
            ENTRADA1.consumirDisparo();
            ENTRADA2.consumirDisparo();
        }

        ScreenUtils.clear(0, 0, 0, 1);

        VENTANA_GRAFICA.apply();
        CAMARA.update();
        JUEGO.obtenerLote().setProjectionMatrix(CAMARA.combined);
        JUEGO.obtenerLote().begin();

        ARENA.dibujar(JUEGO.obtenerLote(), RECURSOS);
        JUGADOR1.dibujar(JUEGO.obtenerLote());
        JUGADOR2.dibujar(JUEGO.obtenerLote());
        for (Proyectil proyectil : PROYECTILES) {
            proyectil.dibujar(JUEGO.obtenerLote());
        }
        for (Efecto efecto : EFECTOS) {
            efecto.dibujar(JUEGO.obtenerLote());
        }

        if (pausado) {
            MENU_PAUSA.dibujar(JUEGO.obtenerLote(), RECURSOS.obtenerTexturaPixel(), JUEGO.obtenerFuente());
        }

        JUEGO.obtenerLote().end();

        HUD.dibujar(JUEGO.obtenerLote(), JUGADOR1, JUGADOR2);
    }

    private void manejarMousePausa() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        VENTANA_GRAFICA.unproject(mouseMundo);

        MENU_PAUSA.actualizarResaltados(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            Accion accion = MENU_PAUSA.manejarClick(mouseMundo.x, mouseMundo.y);
            if (accion == Accion.CONTINUAR) {
                pausado = false;
            } else if (accion == Accion.VOLVER_AL_MENU) {
                JUEGO.setScreen(new PantallaMenu(JUEGO));
            }
        } else if (Gdx.input.isTouched()) {
            MENU_PAUSA.manejarArrastre(mouseMundo.x, mouseMundo.y);
        }
    }

    private void actualizar(float delta) {
        manejarJugador(delta, ENTRADA1, JUGADOR1, JUGADOR2);
        manejarJugador(delta, ENTRADA2, JUGADOR2, JUGADOR1);

        JUGADOR1.actualizar(delta);
        JUGADOR2.actualizar(delta);

        actualizarProyectiles(delta);
        actualizarEfectos(delta);
        revisarFinDePartida(delta);
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
            tanque.avanzar(delta, ARENA, otro);
        } else if (avance < 0) {
            tanque.retroceder(delta, ARENA, otro);
        }

        if (entrada.consumirDisparo()) {
            boolean esJugador1 = tanque == JUGADOR1;
            Animation<TextureRegion> animacionVuelo = esJugador1
                ? RECURSOS.obtenerAnimacionProyectilRojo() : RECURSOS.obtenerAnimacionProyectilAzul();
            Texture flash = esJugador1 ? RECURSOS.obtenerTexturaFlashRojo() : RECURSOS.obtenerTexturaFlashAzul();

            Vector2 posicionCanon = tanque.obtenerPosicionCanon();
            PROYECTILES.add(new Proyectil(posicionCanon, tanque.obtenerAngulo(), tanque, animacionVuelo));
            EFECTOS.add(new Efecto(flash, posicionCanon, 16, 16));
            SONIDOS.obtenerSonidoDisparo().play(ControlVolumen.obtenerVolumenEfectivo());
        }
    }

    private void actualizarProyectiles(float delta) {
        for (int i = PROYECTILES.size - 1; i >= 0; i--) {
            Proyectil proyectil = PROYECTILES.get(i);
            proyectil.actualizar(delta);

            boolean debeEliminarse = false;

            if (!ARENA.estaDentroDeLimites(proyectil.obtenerLimites()) || ARENA.impactarObstaculo(proyectil.obtenerLimites())) {
                debeEliminarse = true;
                agregarEfectoImpacto(proyectil);
            } else {
                Tanque objetivo = proyectil.obtenerPropietario() == JUGADOR1 ? JUGADOR2 : JUGADOR1;
                if (objetivo.estaVivo() && proyectil.obtenerLimites().overlaps(objetivo.obtenerLimites())) {
                    objetivo.recibirImpacto();
                    debeEliminarse = true;
                    agregarEfectoImpacto(proyectil);

                    if (!objetivo.estaVivo()) {
                        objetivo.iniciarExplosion();
                        ganadorPendiente = proyectil.obtenerPropietario().obtenerNombre();
                        SONIDOS.obtenerSonidoMuerte().play(ControlVolumen.obtenerVolumenEfectivo());
                    }
                }
            }

            if (debeEliminarse) {
                PROYECTILES.removeIndex(i);
            }
        }
    }

    private void agregarEfectoImpacto(Proyectil proyectil) {
        boolean esJugador1 = proyectil.obtenerPropietario() == JUGADOR1;
        Texture impacto = esJugador1 ? RECURSOS.obtenerTexturaImpactoRojo() : RECURSOS.obtenerTexturaImpactoAzul();
        EFECTOS.add(new Efecto(impacto, proyectil.obtenerCentro(), 16, 16));
        SONIDOS.obtenerSonidoImpacto().play(ControlVolumen.obtenerVolumenEfectivo());
    }

    private void actualizarEfectos(float delta) {
        for (int i = EFECTOS.size - 1; i >= 0; i--) {
            Efecto efecto = EFECTOS.get(i);
            efecto.actualizar(delta);
            if (efecto.estaFinalizado()) {
                EFECTOS.removeIndex(i);
            }
        }
    }

    private void revisarFinDePartida(float delta) {
        if (ganadorPendiente == null) {
            return;
        }
        Tanque tanqueDestruido = JUGADOR1.estaVivo() ? JUGADOR2 : JUGADOR1;
        if (!tanqueDestruido.explosionTerminada()) {
            return;
        }
        tiempoEsperaFinPartida += delta;
        if (tiempoEsperaFinPartida >= ConfiguracionJuego.RETRASO_FIN_PARTIDA) {
            JUEGO.setScreen(new PantallaFinPartida(JUEGO, ganadorPendiente));
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
