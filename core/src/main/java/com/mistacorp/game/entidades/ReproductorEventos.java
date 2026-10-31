package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.RecursosSonido;
import com.mistacorp.game.compartido.TipoEvento;

public class ReproductorEventos {

    private static final float TAMANO_EFECTO = 16f;

    private final RecursosGraficos RECURSOS;
    private final RecursosSonido SONIDOS;
    private final Array<Efecto> EFECTOS;
    private final DibujoTanque DIBUJO_JUGADOR1;
    private final DibujoTanque DIBUJO_JUGADOR2;

    public ReproductorEventos(RecursosGraficos recursos, RecursosSonido sonidos, Array<Efecto> efectos,
                              DibujoTanque dibujoJugador1, DibujoTanque dibujoJugador2) {
        this.RECURSOS = recursos;
        this.SONIDOS = sonidos;
        this.EFECTOS = efectos;
        this.DIBUJO_JUGADOR1 = dibujoJugador1;
        this.DIBUJO_JUGADOR2 = dibujoJugador2;
    }

    public void reproducir(TipoEvento tipo, float x, float y, int numeroJugador) {
        Vector2 posicion = new Vector2(x, y);
        boolean esJugador1 = numeroJugador == 1;
        float volumen = ControlVolumen.obtenerVolumenEfectivo();
        switch (tipo) {
            case DISPARO:
                Texture flash = esJugador1 ? RECURSOS.obtenerTexturaFlashRojo() : RECURSOS.obtenerTexturaFlashAzul();
                EFECTOS.add(new Efecto(flash, posicion, TAMANO_EFECTO, TAMANO_EFECTO));
                SONIDOS.obtenerSonidoDisparo().play(volumen);
                break;
            case IMPACTO:
                Texture impacto = esJugador1 ? RECURSOS.obtenerTexturaImpactoRojo() : RECURSOS.obtenerTexturaImpactoAzul();
                EFECTOS.add(new Efecto(impacto, posicion, TAMANO_EFECTO, TAMANO_EFECTO));
                SONIDOS.obtenerSonidoImpacto().play(volumen);
                break;
            case MUERTE:
                DibujoTanque destruido = esJugador1 ? DIBUJO_JUGADOR1 : DIBUJO_JUGADOR2;
                if (!destruido.estaExplotando()) {
                    destruido.iniciarExplosion();
                    SONIDOS.obtenerSonidoMuerte().play(volumen);
                }
                break;
            default:
                break;
        }
    }
}
