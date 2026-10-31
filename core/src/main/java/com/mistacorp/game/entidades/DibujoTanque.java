package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.compartido.entidades.EstadoTanque;

public class DibujoTanque {

    private final EstadoTanque ESTADO;
    private final Texture TEXTURA_NORMAL;
    private final Texture TEXTURA_DESTRUIDO;
    private final Animation<TextureRegion> ANIMACION_EXPLOSION;
    private final float ANGULO_BASE_SPRITE;
    private float tiempoExplosion = -1f;

    public DibujoTanque(EstadoTanque estado, Texture texturaNormal, Texture texturaDestruido,
                        Animation<TextureRegion> animacionExplosion, float anguloBaseSprite) {
        this.ESTADO = estado;
        this.TEXTURA_NORMAL = texturaNormal;
        this.TEXTURA_DESTRUIDO = texturaDestruido;
        this.ANIMACION_EXPLOSION = animacionExplosion;
        this.ANGULO_BASE_SPRITE = anguloBaseSprite;
    }

    public void iniciarExplosion() {
        tiempoExplosion = 0f;
    }

    public boolean estaExplotando() {
        return tiempoExplosion >= 0;
    }

    public boolean explosionTerminada() {
        return tiempoExplosion >= 0 && ANIMACION_EXPLOSION.isAnimationFinished(tiempoExplosion);
    }

    public void actualizar(float delta) {
        if (tiempoExplosion >= 0) {
            tiempoExplosion += delta;
        }
    }

    public void dibujar(SpriteBatch lote) {
        if (tiempoExplosion >= 0) {
            dibujarExplosionOCarcasa(lote);
        } else {
            dibujarNormal(lote);
        }
    }

    private void dibujarNormal(SpriteBatch lote) {
        Vector2 posicion = ESTADO.obtenerPosicion();
        Rectangle limites = ESTADO.obtenerLimites();
        float origen = limites.width / 2f;
        float rotacion = ESTADO.obtenerAngulo() - ANGULO_BASE_SPRITE;
        lote.draw(TEXTURA_NORMAL, posicion.x, posicion.y, origen, origen,
            limites.width, limites.height, 1f, 1f, rotacion,
            0, 0, TEXTURA_NORMAL.getWidth(), TEXTURA_NORMAL.getHeight(), false, false);
    }

    private void dibujarExplosionOCarcasa(SpriteBatch lote) {
        Vector2 posicion = ESTADO.obtenerPosicion();
        Rectangle limites = ESTADO.obtenerLimites();
        if (!ANIMACION_EXPLOSION.isAnimationFinished(tiempoExplosion)) {
            TextureRegion frame = ANIMACION_EXPLOSION.getKeyFrame(tiempoExplosion, false);
            float offsetX = (frame.getRegionWidth() - limites.width) / 2f;
            float offsetY = (frame.getRegionHeight() - limites.height) / 2f;
            lote.draw(frame, posicion.x - offsetX, posicion.y - offsetY,
                frame.getRegionWidth(), frame.getRegionHeight());
        } else {
            lote.draw(TEXTURA_DESTRUIDO, posicion.x, posicion.y, limites.width, limites.height);
        }
    }
}
