package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.EventoPartida;
import com.mistacorp.game.compartido.Partida;
import com.mistacorp.game.compartido.entrada.EntradaJugador;
import com.mistacorp.game.compartido.red.CodificadorEstado;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;

public class HiloPartida extends Thread {

    private static final float DELTA_MAXIMO = 0.1f;

    private final HiloServidor SERVIDOR;
    private final Partida PARTIDA = new Partida();
    private final EntradaJugador ENTRADA1 = new EntradaJugador();
    private final EntradaJugador ENTRADA2 = new EntradaJugador();
    private volatile boolean activo = true;

    public HiloPartida(HiloServidor servidor) {
        this.SERVIDOR = servidor;
    }

    @Override
    public void run() {
        float intervaloEnvio = 1f / ConfiguracionRed.ENVIOS_POR_SEGUNDO;
        float tiempoDesdeEnvio = 0f;
        long anterior = System.nanoTime();
        while (activo) {
            long ahora = System.nanoTime();
            float delta = Math.min((ahora - anterior) / 1000000000f, DELTA_MAXIMO);
            anterior = ahora;

            PARTIDA.actualizar(delta, ENTRADA1, ENTRADA2);
            enviarEventos();

            tiempoDesdeEnvio += delta;
            if (tiempoDesdeEnvio >= intervaloEnvio) {
                tiempoDesdeEnvio -= intervaloEnvio;
                SERVIDOR.enviarATodos(CodificadorEstado.codificar(PARTIDA));
            }
            esperar();
        }
    }

    private void enviarEventos() {
        for (EventoPartida evento : PARTIDA.obtenerEventos()) {
            SERVIDOR.enviarATodos(Protocolo.armar(TipoMensaje.EVENTO,
                evento.obtenerTipo().name(),
                Protocolo.entero(evento.obtenerX()),
                Protocolo.entero(evento.obtenerY()),
                String.valueOf(evento.obtenerNumeroJugador())));
        }
        PARTIDA.limpiarEventos();
    }

    private void esperar() {
        try {
            Thread.sleep(ConfiguracionRed.MILISEGUNDOS_PASO_SIMULACION);
        } catch (InterruptedException e) {
            activo = false;
        }
    }

    public EntradaJugador obtenerEntrada(int numeroJugador) {
        return numeroJugador == 1 ? ENTRADA1 : ENTRADA2;
    }

    public void detener() {
        activo = false;
    }
}
