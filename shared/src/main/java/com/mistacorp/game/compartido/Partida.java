package com.mistacorp.game.compartido;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.compartido.entidades.EstadoProyectil;
import com.mistacorp.game.compartido.entidades.EstadoTanque;
import com.mistacorp.game.compartido.entrada.EntradaJugador;
import com.mistacorp.game.compartido.mundo.Arena;

public class Partida {

    private static final float MARGEN_INICIAL = 60f;

    private final Arena ARENA = new Arena();
    private final EstadoTanque JUGADOR1;
    private final EstadoTanque JUGADOR2;
    private final Array<EstadoProyectil> PROYECTILES = new Array<>();
    private final Array<EventoPartida> EVENTOS = new Array<>();
    private EstadoTanque ganador = null;

    public Partida() {
        float posicionY = ConfiguracionPartida.ALTO_MUNDO / 2f - ConfiguracionPartida.TAMANO_TANQUE / 2f;
        float posicionXJugador2 = ConfiguracionPartida.ANCHO_MUNDO - MARGEN_INICIAL - ConfiguracionPartida.TAMANO_TANQUE;
        JUGADOR1 = new EstadoTanque(1, "Jugador 1", MARGEN_INICIAL, posicionY, 0f);
        JUGADOR2 = new EstadoTanque(2, "Jugador 2", posicionXJugador2, posicionY, 180f);
    }

    public void actualizar(float delta, EntradaJugador entrada1, EntradaJugador entrada2) {
        manejarJugador(delta, entrada1, JUGADOR1, JUGADOR2);
        manejarJugador(delta, entrada2, JUGADOR2, JUGADOR1);
        actualizarProyectiles(delta);
    }

    private void manejarJugador(float delta, EntradaJugador entrada, EstadoTanque tanque, EstadoTanque otro) {
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
            Vector2 posicionCanon = tanque.obtenerPosicionCanon();
            PROYECTILES.add(new EstadoProyectil(posicionCanon, tanque.obtenerAngulo(), tanque));
            EVENTOS.add(new EventoPartida(TipoEvento.DISPARO, posicionCanon.x, posicionCanon.y, tanque.obtenerNumeroJugador()));
        }
    }

    private void actualizarProyectiles(float delta) {
        for (int i = PROYECTILES.size - 1; i >= 0; i--) {
            EstadoProyectil proyectil = PROYECTILES.get(i);
            proyectil.actualizar(delta);

            boolean debeEliminarse = false;

            if (!ARENA.estaDentroDeLimites(proyectil.obtenerLimites()) || ARENA.impactarObstaculo(proyectil.obtenerLimites())) {
                debeEliminarse = true;
                agregarEventoImpacto(proyectil);
            } else {
                EstadoTanque objetivo = proyectil.obtenerPropietario() == JUGADOR1 ? JUGADOR2 : JUGADOR1;
                if (objetivo.estaVivo() && proyectil.obtenerLimites().overlaps(objetivo.obtenerLimites())) {
                    objetivo.recibirImpacto();
                    debeEliminarse = true;
                    agregarEventoImpacto(proyectil);

                    if (!objetivo.estaVivo()) {
                        ganador = proyectil.obtenerPropietario();
                        Vector2 centro = proyectil.obtenerCentro();
                        EVENTOS.add(new EventoPartida(TipoEvento.MUERTE, centro.x, centro.y, objetivo.obtenerNumeroJugador()));
                    }
                }
            }

            if (debeEliminarse) {
                PROYECTILES.removeIndex(i);
            }
        }
    }

    private void agregarEventoImpacto(EstadoProyectil proyectil) {
        Vector2 centro = proyectil.obtenerCentro();
        int numeroJugador = proyectil.obtenerPropietario().obtenerNumeroJugador();
        EVENTOS.add(new EventoPartida(TipoEvento.IMPACTO, centro.x, centro.y, numeroJugador));
    }

    public Array<EventoPartida> obtenerEventos() {
        return EVENTOS;
    }

    public void limpiarEventos() {
        EVENTOS.clear();
    }

    public Arena obtenerArena() {
        return ARENA;
    }

    public EstadoTanque obtenerJugador1() {
        return JUGADOR1;
    }

    public EstadoTanque obtenerJugador2() {
        return JUGADOR2;
    }

    public Array<EstadoProyectil> obtenerProyectiles() {
        return PROYECTILES;
    }

    public EstadoTanque obtenerGanador() {
        return ganador;
    }
}
