package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;

public class Proyectil {

    private final Vector2 posicion;
    private final Vector2 velocidad;
    private final Rectangle limites;
    private final Tanque propietario;
    private final float angulo;
    private final Animation<TextureRegion> animacionVuelo;
    private float tiempoTranscurrido = 0f;

    public Proyectil(Vector2 posicionInicial, float angulo, Tanque propietario,
                      Animation<TextureRegion> animacionVuelo) {
        this.posicion = new Vector2(
            posicionInicial.x - ConfiguracionJuego.TAMANO_PROYECTIL / 2f,
            posicionInicial.y - ConfiguracionJuego.TAMANO_PROYECTIL / 2f
        );
        this.angulo = angulo;
        this.velocidad = new Vector2(MathUtils.cosDeg(angulo), MathUtils.sinDeg(angulo))
            .scl(ConfiguracionJuego.VELOCIDAD_PROYECTIL);
        this.limites = new Rectangle(posicion.x, posicion.y, ConfiguracionJuego.TAMANO_PROYECTIL, ConfiguracionJuego.TAMANO_PROYECTIL);
        this.propietario = propietario;
        this.animacionVuelo = animacionVuelo;
    }

    public void actualizar(float delta) {
        posicion.mulAdd(velocidad, delta);
        limites.setPosition(posicion.x, posicion.y);
        tiempoTranscurrido += delta;
    }

    public void dibujar(SpriteBatch lote) {
        TextureRegion frame = animacionVuelo.getKeyFrame(tiempoTranscurrido, true);
        float origen = limites.width / 2f;
        lote.draw(frame, posicion.x, posicion.y, origen, origen,
            limites.width, limites.height, 1f, 1f, angulo);
    }

    public Vector2 obtenerCentro() {
        return new Vector2(posicion.x + limites.width / 2f, posicion.y + limites.height / 2f);
    }

    public Rectangle obtenerLimites() {
        return limites;
    }

    public Tanque obtenerPropietario() {
        return propietario;
    }
}
