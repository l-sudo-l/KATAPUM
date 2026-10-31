package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.compartido.entidades.EstadoProyectil;

public class DibujoProyectil {

    private final Animation<TextureRegion> ANIMACION_JUGADOR1;
    private final Animation<TextureRegion> ANIMACION_JUGADOR2;
    private float tiempoTranscurrido = 0f;

    public DibujoProyectil(Animation<TextureRegion> animacionJugador1, Animation<TextureRegion> animacionJugador2) {
        this.ANIMACION_JUGADOR1 = animacionJugador1;
        this.ANIMACION_JUGADOR2 = animacionJugador2;
    }

    public void actualizar(float delta) {
        tiempoTranscurrido += delta;
    }

    public void dibujar(SpriteBatch lote, EstadoProyectil proyectil) {
        Animation<TextureRegion> animacion = proyectil.obtenerPropietario().obtenerNumeroJugador() == 1
            ? ANIMACION_JUGADOR1 : ANIMACION_JUGADOR2;
        TextureRegion frame = animacion.getKeyFrame(tiempoTranscurrido, true);
        Vector2 posicion = proyectil.obtenerPosicion();
        Rectangle limites = proyectil.obtenerLimites();
        float origen = limites.width / 2f;
        lote.draw(frame, posicion.x, posicion.y, origen, origen,
            limites.width, limites.height, 1f, 1f, proyectil.obtenerAngulo());
    }
}
