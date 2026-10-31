package com.mistacorp.game.compartido.entidades;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.compartido.ConfiguracionPartida;

public class EstadoProyectil {

    private final Vector2 POSICION;
    private final Vector2 VELOCIDAD;
    private final Rectangle LIMITES;
    private final EstadoTanque PROPIETARIO;
    private final float ANGULO;

    public EstadoProyectil(Vector2 posicionInicial, float angulo, EstadoTanque propietario) {
        this.POSICION = new Vector2(
            posicionInicial.x - ConfiguracionPartida.TAMANO_PROYECTIL / 2f,
            posicionInicial.y - ConfiguracionPartida.TAMANO_PROYECTIL / 2f
        );
        this.ANGULO = angulo;
        this.VELOCIDAD = new Vector2(MathUtils.cosDeg(angulo), MathUtils.sinDeg(angulo))
            .scl(ConfiguracionPartida.VELOCIDAD_PROYECTIL);
        this.LIMITES = new Rectangle(POSICION.x, POSICION.y, ConfiguracionPartida.TAMANO_PROYECTIL, ConfiguracionPartida.TAMANO_PROYECTIL);
        this.PROPIETARIO = propietario;
    }

    public void actualizar(float delta) {
        POSICION.mulAdd(VELOCIDAD, delta);
        LIMITES.setPosition(POSICION.x, POSICION.y);
    }

    public Vector2 obtenerCentro() {
        return new Vector2(POSICION.x + LIMITES.width / 2f, POSICION.y + LIMITES.height / 2f);
    }

    public Vector2 obtenerPosicion() {
        return POSICION;
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }

    public float obtenerAngulo() {
        return ANGULO;
    }

    public EstadoTanque obtenerPropietario() {
        return PROPIETARIO;
    }
}
