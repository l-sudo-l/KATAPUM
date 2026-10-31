package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.EventoPartida;
import com.mistacorp.game.compartido.Partida;
import com.mistacorp.game.compartido.entidades.EstadoProyectil;
import com.mistacorp.game.compartido.entidades.EstadoTanque;
import com.mistacorp.game.entidades.DibujoProyectil;
import com.mistacorp.game.entidades.DibujoTanque;
import com.mistacorp.game.entidades.Efecto;
import com.mistacorp.game.entrada.ManejadorEntradaJugador;
import com.mistacorp.game.interfaz.Accion;
import com.mistacorp.game.interfaz.Hud;
import com.mistacorp.game.interfaz.MenuPausa;
import com.mistacorp.game.mundo.DibujoArena;

public class PantallaJuego extends ScreenAdapter {

    private final KatapumPrincipal JUEGO;
    private final RecursosGraficos RECURSOS;
    private final RecursosSonido SONIDOS;

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO, CAMARA);

    private final Partida PARTIDA = new Partida();
    private final DibujoArena DIBUJO_ARENA;
    private final DibujoTanque DIBUJO_JUGADOR1;
    private final DibujoTanque DIBUJO_JUGADOR2;
    private final DibujoProyectil DIBUJO_PROYECTIL;
    private final Array<Efecto> EFECTOS = new Array<>();

    private final ManejadorEntradaJugador ENTRADA1;
    private final ManejadorEntradaJugador ENTRADA2;
    private final Hud HUD;
    private final MenuPausa MENU_PAUSA;

    private boolean pausado = false;
    private float tiempoEsperaFinPartida = 0f;

    public PantallaJuego(KatapumPrincipal juego) {
        this.JUEGO = juego;
        this.RECURSOS = juego.obtenerRecursos();
        this.SONIDOS = juego.obtenerSonidos();
        this.DIBUJO_ARENA = new DibujoArena(RECURSOS);
        this.DIBUJO_PROYECTIL = new DibujoProyectil(RECURSOS.obtenerAnimacionProyectilRojo(),
            RECURSOS.obtenerAnimacionProyectilAzul());

        DIBUJO_JUGADOR1 = new DibujoTanque(PARTIDA.obtenerJugador1(), RECURSOS.obtenerTexturaTanqueVerde(),
            RECURSOS.obtenerTexturaTanqueVerdeDestruido(), RECURSOS.obtenerAnimacionExplosionVerde(), 0f);
        DIBUJO_JUGADOR2 = new DibujoTanque(PARTIDA.obtenerJugador2(), RECURSOS.obtenerTexturaTanqueAzul(),
            RECURSOS.obtenerTexturaTanqueAzulDestruido(), RECURSOS.obtenerAnimacionExplosionAzul(), 0f);

        ENTRADA1 = new ManejadorEntradaJugador(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        ENTRADA2 = new ManejadorEntradaJugador(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Input.Keys.CONTROL_RIGHT);

        HUD = new Hud(juego.obtenerFuente(), RECURSOS);
        MENU_PAUSA = new MenuPausa(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO);
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
            ENTRADA1.obtenerEntrada().consumirDisparo();
            ENTRADA2.obtenerEntrada().consumirDisparo();
        }

        ScreenUtils.clear(0, 0, 0, 1);

        VENTANA_GRAFICA.apply();
        CAMARA.update();
        JUEGO.obtenerLote().setProjectionMatrix(CAMARA.combined);
        JUEGO.obtenerLote().begin();

        DIBUJO_ARENA.dibujar(JUEGO.obtenerLote(), PARTIDA.obtenerArena());
        DIBUJO_JUGADOR1.dibujar(JUEGO.obtenerLote());
        DIBUJO_JUGADOR2.dibujar(JUEGO.obtenerLote());
        for (EstadoProyectil proyectil : PARTIDA.obtenerProyectiles()) {
            DIBUJO_PROYECTIL.dibujar(JUEGO.obtenerLote(), proyectil);
        }
        for (Efecto efecto : EFECTOS) {
            efecto.dibujar(JUEGO.obtenerLote());
        }

        if (pausado) {
            MENU_PAUSA.dibujar(JUEGO.obtenerLote(), RECURSOS.obtenerTexturaPixel(), JUEGO.obtenerFuente());
        }

        JUEGO.obtenerLote().end();

        HUD.dibujar(JUEGO.obtenerLote(), PARTIDA.obtenerJugador1(), PARTIDA.obtenerJugador2());
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
        PARTIDA.actualizar(delta, ENTRADA1.obtenerEntrada(), ENTRADA2.obtenerEntrada());

        DIBUJO_JUGADOR1.actualizar(delta);
        DIBUJO_JUGADOR2.actualizar(delta);
        DIBUJO_PROYECTIL.actualizar(delta);

        procesarEventos();
        actualizarEfectos(delta);
        revisarFinDePartida(delta);
    }

    private void procesarEventos() {
        for (EventoPartida evento : PARTIDA.obtenerEventos()) {
            Vector2 posicion = new Vector2(evento.obtenerX(), evento.obtenerY());
            boolean esJugador1 = evento.obtenerNumeroJugador() == 1;
            float volumen = ControlVolumen.obtenerVolumenEfectivo();
            switch (evento.obtenerTipo()) {
                case DISPARO:
                    Texture flash = esJugador1 ? RECURSOS.obtenerTexturaFlashRojo() : RECURSOS.obtenerTexturaFlashAzul();
                    EFECTOS.add(new Efecto(flash, posicion, 16, 16));
                    SONIDOS.obtenerSonidoDisparo().play(volumen);
                    break;
                case IMPACTO:
                    Texture impacto = esJugador1 ? RECURSOS.obtenerTexturaImpactoRojo() : RECURSOS.obtenerTexturaImpactoAzul();
                    EFECTOS.add(new Efecto(impacto, posicion, 16, 16));
                    SONIDOS.obtenerSonidoImpacto().play(volumen);
                    break;
                case MUERTE:
                    DibujoTanque destruido = esJugador1 ? DIBUJO_JUGADOR1 : DIBUJO_JUGADOR2;
                    destruido.iniciarExplosion();
                    SONIDOS.obtenerSonidoMuerte().play(volumen);
                    break;
                default:
                    break;
            }
        }
        PARTIDA.limpiarEventos();
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
        EstadoTanque ganador = PARTIDA.obtenerGanador();
        if (ganador == null) {
            return;
        }
        DibujoTanque tanqueDestruido = ganador.obtenerNumeroJugador() == 1 ? DIBUJO_JUGADOR2 : DIBUJO_JUGADOR1;
        if (!tanqueDestruido.explosionTerminada()) {
            return;
        }
        tiempoEsperaFinPartida += delta;
        if (tiempoEsperaFinPartida >= ConfiguracionJuego.RETRASO_FIN_PARTIDA) {
            JUEGO.setScreen(new PantallaFinPartida(JUEGO, ganador.obtenerNombre()));
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
