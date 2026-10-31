package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mistacorp.game.compartido.Partida;
import com.mistacorp.game.compartido.TipoEvento;
import com.mistacorp.game.compartido.entidades.EstadoProyectil;
import com.mistacorp.game.compartido.entidades.EstadoTanque;
import com.mistacorp.game.compartido.entrada.EntradaJugador;
import com.mistacorp.game.compartido.red.CodificadorEstado;
import com.mistacorp.game.compartido.red.MotivoFin;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;
import com.mistacorp.game.entidades.DibujoProyectil;
import com.mistacorp.game.entidades.DibujoTanque;
import com.mistacorp.game.entidades.Efecto;
import com.mistacorp.game.entidades.ReproductorEventos;
import com.mistacorp.game.entrada.ManejadorEntradaJugador;
import com.mistacorp.game.interfaz.Hud;
import com.mistacorp.game.mundo.DibujoArena;
import com.mistacorp.game.red.HiloCliente;
import com.mistacorp.game.red.OyenteRed;

public class PantallaJuegoRed extends ScreenAdapter implements OyenteRed {

    private final KatapumPrincipal JUEGO;
    private final RecursosGraficos RECURSOS;
    private final RecursosSonido SONIDOS;
    private final HiloCliente CLIENTE;
    private final int NUMERO_JUGADOR;

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO, CAMARA);

    private final Partida PARTIDA = new Partida();
    private final DibujoArena DIBUJO_ARENA;
    private final DibujoTanque DIBUJO_JUGADOR1;
    private final DibujoTanque DIBUJO_JUGADOR2;
    private final DibujoProyectil DIBUJO_PROYECTIL;
    private final Array<Efecto> EFECTOS = new Array<>();
    private final ReproductorEventos REPRODUCTOR;

    private final ManejadorEntradaJugador ENTRADA;
    private final Hud HUD;

    private int contadorDisparos = 0;
    private int secuenciaEntrada = 0;
    private int ultimaSecuenciaEstado = 0;
    private float tiempoDesdeEnvio = 0f;
    private int numeroGanador = 0;
    private boolean rivalSeFue = false;
    private float tiempoEsperaFin = 0f;
    private float tiempoSinEstado = 0f;

    public PantallaJuegoRed(KatapumPrincipal juego, HiloCliente cliente, int numeroJugador) {
        this.JUEGO = juego;
        this.RECURSOS = juego.obtenerRecursos();
        this.SONIDOS = juego.obtenerSonidos();
        this.CLIENTE = cliente;
        this.NUMERO_JUGADOR = numeroJugador;

        DIBUJO_ARENA = new DibujoArena(RECURSOS);
        DIBUJO_PROYECTIL = new DibujoProyectil(RECURSOS.obtenerAnimacionProyectilRojo(),
            RECURSOS.obtenerAnimacionProyectilAzul());
        DIBUJO_JUGADOR1 = new DibujoTanque(PARTIDA.obtenerJugador1(), RECURSOS.obtenerTexturaTanqueVerde(),
            RECURSOS.obtenerTexturaTanqueVerdeDestruido(), RECURSOS.obtenerAnimacionExplosionVerde(), 0f);
        DIBUJO_JUGADOR2 = new DibujoTanque(PARTIDA.obtenerJugador2(), RECURSOS.obtenerTexturaTanqueAzul(),
            RECURSOS.obtenerTexturaTanqueAzulDestruido(), RECURSOS.obtenerAnimacionExplosionAzul(), 0f);
        REPRODUCTOR = new ReproductorEventos(RECURSOS, SONIDOS, EFECTOS, DIBUJO_JUGADOR1, DIBUJO_JUGADOR2);

        ENTRADA = new ManejadorEntradaJugador(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        HUD = new Hud(juego.obtenerFuente(), RECURSOS);

        CLIENTE.establecerOyente(this);
        CLIENTE.marcarPartidaIniciada();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(ENTRADA);
        SONIDOS.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());
        SONIDOS.obtenerMusicaJuego().play();
    }

    @Override
    public void hide() {
        SONIDOS.obtenerMusicaJuego().stop();
    }

    @Override
    public void dispose() {
        CLIENTE.detener();
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
        HUD.redimensionar(ancho, alto);
    }

    @Override
    public void mensajeRecibido(TipoMensaje tipo, String[] partes) {
        switch (tipo) {
            case ESTADO:
                int secuencia = CodificadorEstado.leerSecuencia(partes);
                if (secuencia <= ultimaSecuenciaEstado) {
                    return;
                }
                ultimaSecuenciaEstado = secuencia;
                tiempoSinEstado = 0f;
                CodificadorEstado.aplicar(partes, PARTIDA);
                revisarTanqueSinVidas(PARTIDA.obtenerJugador1(), DIBUJO_JUGADOR1);
                revisarTanqueSinVidas(PARTIDA.obtenerJugador2(), DIBUJO_JUGADOR2);
                break;
            case RIVAL_SALIO:
                rivalSeFue = true;
                break;
            case FIN:
                registrarFin(Integer.parseInt(partes[1]), MotivoFin.values()[Integer.parseInt(partes[2])]);
                break;
            case EVENTO:
                REPRODUCTOR.reproducir(TipoEvento.valueOf(partes[1]), Float.parseFloat(partes[2]),
                    Float.parseFloat(partes[3]), Integer.parseInt(partes[4]));
                break;
            default:
                break;
        }
    }

    private void registrarFin(int ganador, MotivoFin motivo) {
        if (numeroGanador != 0) {
            return;
        }
        numeroGanador = ganador;
        if (motivo == MotivoFin.RIVAL_DESCONECTADO) {
            rivalSeFue = true;
        }
    }

    private void revisarFinDePartida(float delta) {
        if (numeroGanador == 0) {
            return;
        }
        DibujoTanque perdedor = numeroGanador == 1 ? DIBUJO_JUGADOR2 : DIBUJO_JUGADOR1;
        if (!rivalSeFue && !perdedor.explosionTerminada()) {
            return;
        }
        tiempoEsperaFin += delta;
        if (tiempoEsperaFin >= ConfiguracionJuego.RETRASO_FIN_PARTIDA) {
            JUEGO.setScreen(new PantallaFinPartidaRed(JUEGO, CLIENTE, NUMERO_JUGADOR, numeroGanador, rivalSeFue));
        }
    }

    private void revisarConexionConServidor(float delta) {
        if (numeroGanador != 0) {
            return;
        }
        tiempoSinEstado += delta;
        if (tiempoSinEstado * 1000f > ConfiguracionRed.MILISEGUNDOS_DESCONEXION) {
            CLIENTE.detener();
            JUEGO.setScreen(new PantallaConectar(JUEGO, "Se perdio la conexion con el servidor"));
        }
    }

    private void revisarTanqueSinVidas(EstadoTanque tanque, DibujoTanque dibujo) {
        if (!tanque.estaVivo() && !dibujo.estaExplotando()) {
            dibujo.iniciarExplosion();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            volverAlMenu();
            return;
        }
        SONIDOS.obtenerMusicaJuego().setVolume(ControlVolumen.obtenerVolumenEfectivo());

        enviarEntrada(delta);
        DIBUJO_JUGADOR1.actualizar(delta);
        DIBUJO_JUGADOR2.actualizar(delta);
        DIBUJO_PROYECTIL.actualizar(delta);
        actualizarEfectos(delta);
        revisarFinDePartida(delta);
        revisarConexionConServidor(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        VENTANA_GRAFICA.apply();
        CAMARA.update();
        SpriteBatch lote = JUEGO.obtenerLote();
        lote.setProjectionMatrix(CAMARA.combined);
        lote.begin();

        DIBUJO_ARENA.dibujar(lote, PARTIDA.obtenerArena());
        DIBUJO_JUGADOR1.dibujar(lote);
        DIBUJO_JUGADOR2.dibujar(lote);
        for (EstadoProyectil proyectil : PARTIDA.obtenerProyectiles()) {
            DIBUJO_PROYECTIL.dibujar(lote, proyectil);
        }
        for (Efecto efecto : EFECTOS) {
            efecto.dibujar(lote);
        }
        dibujarAyuda(lote);

        lote.end();

        HUD.dibujar(lote, PARTIDA.obtenerJugador1(), PARTIDA.obtenerJugador2());
    }

    private void enviarEntrada(float delta) {
        float intervaloEnvio = 1f / ConfiguracionRed.ENVIOS_POR_SEGUNDO;
        tiempoDesdeEnvio += delta;
        if (tiempoDesdeEnvio < intervaloEnvio) {
            return;
        }
        tiempoDesdeEnvio = Math.min(tiempoDesdeEnvio - intervaloEnvio, intervaloEnvio);

        EntradaJugador entrada = ENTRADA.obtenerEntrada();
        if (entrada.consumirDisparo()) {
            contadorDisparos++;
        }
        int giro = Math.round(entrada.obtenerGiro()) + 1;
        int avance = Math.round(entrada.obtenerAvance()) + 1;
        secuenciaEntrada++;
        CLIENTE.enviar(Protocolo.armar(TipoMensaje.INPUT, String.valueOf(secuenciaEntrada),
            String.valueOf(giro), String.valueOf(avance), String.valueOf(contadorDisparos)));
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

    private void dibujarAyuda(SpriteBatch lote) {
        String texto = "Sos el Jugador " + NUMERO_JUGADOR + "  |  W/S/A/D y ESPACIO  |  ESC: salir";
        GlyphLayout layout = new GlyphLayout(JUEGO.obtenerFuente(), texto);
        JUEGO.obtenerFuente().draw(lote, texto, ConfiguracionPartida.ANCHO_MUNDO / 2f - layout.width / 2f, 24f);
    }

    private void volverAlMenu() {
        CLIENTE.detener();
        JUEGO.setScreen(new PantallaMenu(JUEGO));
    }
}
