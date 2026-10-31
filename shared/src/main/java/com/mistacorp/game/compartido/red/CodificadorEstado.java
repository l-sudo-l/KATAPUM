package com.mistacorp.game.compartido.red;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.compartido.Partida;
import com.mistacorp.game.compartido.entidades.EstadoProyectil;
import com.mistacorp.game.compartido.entidades.EstadoTanque;
import com.mistacorp.game.compartido.mundo.Decoracion;
import com.mistacorp.game.compartido.mundo.Pared;

public final class CodificadorEstado {

    private CodificadorEstado() {
    }

    public static String codificar(Partida partida, int secuencia) {
        StringBuilder datos = new StringBuilder(TipoMensaje.ESTADO.name());
        agregar(datos, String.valueOf(secuencia));
        agregarTanque(datos, partida.obtenerJugador1());
        agregarTanque(datos, partida.obtenerJugador2());
        for (Pared pared : partida.obtenerArena().obtenerParedes()) {
            agregar(datos, String.valueOf(pared.obtenerImpactosRecibidos()));
        }
        for (Decoracion decoracion : partida.obtenerArena().obtenerDecoraciones()) {
            agregar(datos, String.valueOf(decoracion.obtenerImpactosRecibidos()));
        }
        Array<EstadoProyectil> proyectiles = partida.obtenerProyectiles();
        agregar(datos, String.valueOf(proyectiles.size));
        for (int i = 0; i < proyectiles.size; i++) {
            EstadoProyectil proyectil = proyectiles.get(i);
            Vector2 centro = proyectil.obtenerCentro();
            agregar(datos, Protocolo.entero(centro.x));
            agregar(datos, Protocolo.entero(centro.y));
            agregar(datos, Protocolo.entero(proyectil.obtenerAngulo()));
            agregar(datos, String.valueOf(proyectil.obtenerPropietario().obtenerNumeroJugador()));
        }
        return datos.toString();
    }

    private static void agregarTanque(StringBuilder datos, EstadoTanque tanque) {
        agregar(datos, Protocolo.entero(tanque.obtenerPosicion().x));
        agregar(datos, Protocolo.entero(tanque.obtenerPosicion().y));
        agregar(datos, Protocolo.entero(tanque.obtenerAngulo()));
        agregar(datos, String.valueOf(tanque.obtenerVidas()));
    }

    private static void agregar(StringBuilder datos, String valor) {
        datos.append(ConfiguracionRed.SEPARADOR).append(valor);
    }

    public static int leerSecuencia(String[] partes) {
        return Integer.parseInt(partes[1]);
    }

    public static void aplicar(String[] partes, Partida partida) {
        int indice = 2;
        indice = leerTanque(partes, indice, partida.obtenerJugador1());
        indice = leerTanque(partes, indice, partida.obtenerJugador2());
        for (Pared pared : partida.obtenerArena().obtenerParedes()) {
            pared.establecerImpactos(Integer.parseInt(partes[indice]));
            indice++;
        }
        for (Decoracion decoracion : partida.obtenerArena().obtenerDecoraciones()) {
            decoracion.establecerImpactos(Integer.parseInt(partes[indice]));
            indice++;
        }
        int cantidadProyectiles = Integer.parseInt(partes[indice]);
        indice++;
        Array<EstadoProyectil> proyectiles = partida.obtenerProyectiles();
        proyectiles.clear();
        for (int i = 0; i < cantidadProyectiles; i++) {
            float x = Float.parseFloat(partes[indice]);
            float y = Float.parseFloat(partes[indice + 1]);
            float angulo = Float.parseFloat(partes[indice + 2]);
            int numeroJugador = Integer.parseInt(partes[indice + 3]);
            indice += 4;
            EstadoTanque propietario = numeroJugador == 1 ? partida.obtenerJugador1() : partida.obtenerJugador2();
            proyectiles.add(new EstadoProyectil(new Vector2(x, y), angulo, propietario));
        }
    }

    private static int leerTanque(String[] partes, int indice, EstadoTanque tanque) {
        float x = Float.parseFloat(partes[indice]);
        float y = Float.parseFloat(partes[indice + 1]);
        float angulo = Float.parseFloat(partes[indice + 2]);
        int vidas = Integer.parseInt(partes[indice + 3]);
        tanque.establecerEstado(x, y, angulo, vidas);
        return indice + 4;
    }
}
