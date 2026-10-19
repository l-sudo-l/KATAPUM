package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;

public class Proyectil {

    private final Vector2 POSICION;
    private final Vector2 VELOCIDAD;
    private final Rectangle LIMITES;
    private final Tanque PROPIETARIO;
    private final float ANGULO;
    private final Animation<TextureRegion> ANIMACION_VUELO;
    private float tiempoTranscurrido = 0f;

    public Proyectil(Vector2 posicionInicial, float angulo, Tanque propietario,
                      Animation<TextureRegion> animacionVuelo) {
        this.POSICION = new Vector2(
            posicionInicial.x - ConfiguracionJuego.TAMANO_PROYECTIL / 2f,
            posicionInicial.y - ConfiguracionJuego.TAMANO_PROYECTIL / 2f
        );
        this.ANGULO = angulo;
        this.VELOCIDAD = new Vector2(MathUtils.cosDeg(angulo), MathUtils.sinDeg(angulo))
            .scl(ConfiguracionJuego.VELOCIDAD_PROYECTIL);
        this.LIMITES = new Rectangle(POSICION.x, POSICION.y, ConfiguracionJuego.TAMANO_PROYECTIL, ConfiguracionJuego.TAMANO_PROYECTIL);
        this.PROPIETARIO = propietario;
        this.ANIMACION_VUELO = animacionVuelo;
    }

    public void actualizar(float delta) {
        POSICION.mulAdd(VELOCIDAD, delta);
        LIMITES.setPosition(POSICION.x, POSICION.y);
        tiempoTranscurrido += delta;
    }

    public void dibujar(SpriteBatch lote) {
        TextureRegion frame = ANIMACION_VUELO.getKeyFrame(tiempoTranscurrido, true);
        float origen = LIMITES.width / 2f;
        lote.draw(frame, POSICION.x, POSICION.y, origen, origen,
            LIMITES.width, LIMITES.height, 1f, 1f, ANGULO);
    }

    public Vector2 obtenerCentro() {
        return new Vector2(POSICION.x + LIMITES.width / 2f, POSICION.y + LIMITES.height / 2f);
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }

    public Tanque obtenerPropietario() {
        return PROPIETARIO;
    }
}
