package com.mistacorp.game.compartido.entidades;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.mundo.Arena;

public class EstadoTanque {

    private final int NUMERO_JUGADOR;
    private final String NOMBRE;
    private final Vector2 POSICION;
    private final Rectangle LIMITES;
    private float angulo;
    private int vidas;

    public EstadoTanque(int numeroJugador, String nombre, float x, float y, float anguloInicial) {
        this.NUMERO_JUGADOR = numeroJugador;
        this.NOMBRE = nombre;
        this.POSICION = new Vector2(x, y);
        this.LIMITES = new Rectangle(x, y, ConfiguracionPartida.TAMANO_TANQUE, ConfiguracionPartida.TAMANO_TANQUE);
        this.angulo = anguloInicial;
        this.vidas = ConfiguracionPartida.VIDAS_MAXIMAS;
    }

    public void avanzar(float delta, Arena arena, EstadoTanque otro) {
        mover(1f, delta, arena, otro);
    }

    public void retroceder(float delta, Arena arena, EstadoTanque otro) {
        mover(-1f, delta, arena, otro);
    }

    public void rotar(float sentido, float delta) {
        angulo += sentido * ConfiguracionPartida.VELOCIDAD_ROTACION * delta;
        angulo = ((angulo % 360f) + 360f) % 360f;
    }

    private void mover(float signo, float delta, Arena arena, EstadoTanque otro) {
        float distancia = ConfiguracionPartida.VELOCIDAD_TANQUE * delta * signo;
        float dx = MathUtils.cosDeg(angulo) * distancia;
        float dy = MathUtils.sinDeg(angulo) * distancia;

        float nuevoX = POSICION.x + dx;
        Rectangle intentoX = new Rectangle(nuevoX, POSICION.y, LIMITES.width, LIMITES.height);
        if (estaLibre(intentoX, arena, otro)) {
            POSICION.x = nuevoX;
        }

        float nuevoY = POSICION.y + dy;
        Rectangle intentoY = new Rectangle(POSICION.x, nuevoY, LIMITES.width, LIMITES.height);
        if (estaLibre(intentoY, arena, otro)) {
            POSICION.y = nuevoY;
        }

        LIMITES.setPosition(POSICION.x, POSICION.y);
    }

    private boolean estaLibre(Rectangle intento, Arena arena, EstadoTanque otro) {
        return arena.estaDentroDeLimites(intento)
            && !arena.colisionaConObstaculos(intento)
            && !intento.overlaps(otro.LIMITES);
    }

    public Vector2 obtenerPosicionCanon() {
        float centroX = POSICION.x + LIMITES.width / 2f;
        float centroY = POSICION.y + LIMITES.height / 2f;
        float desplazamiento = LIMITES.width / 2f + 4f;
        return new Vector2(
            centroX + MathUtils.cosDeg(angulo) * desplazamiento,
            centroY + MathUtils.sinDeg(angulo) * desplazamiento
        );
    }

    public void recibirImpacto() {
        vidas = Math.max(0, vidas - 1);
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public int obtenerNumeroJugador() {
        return NUMERO_JUGADOR;
    }

    public String obtenerNombre() {
        return NOMBRE;
    }

    public int obtenerVidas() {
        return vidas;
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }

    public Vector2 obtenerPosicion() {
        return POSICION;
    }

    public float obtenerAngulo() {
        return angulo;
    }
}
