package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.mundo.Arena;

public class Tanque {

    private final String NOMBRE;
    private final Vector2 POSICION;
    private final Rectangle LIMITES;
    private float angulo;
    private int vidas;

    private final Texture TEXTURA_NORMAL;
    private final Texture TEXTURA_DESTRUIDO;
    private final Animation<TextureRegion> ANIMACION_EXPLOSION;
    private final float ANGULO_BASE_SPRITE;
    private float tiempoExplosion = -1f;

    public Tanque(String nombre, Texture texturaNormal, Texture texturaDestruido,
                  Animation<TextureRegion> animacionExplosion, float anguloBaseSprite,
                  float x, float y, float anguloInicial) {
        this.NOMBRE = nombre;
        this.TEXTURA_NORMAL = texturaNormal;
        this.TEXTURA_DESTRUIDO = texturaDestruido;
        this.ANIMACION_EXPLOSION = animacionExplosion;
        this.ANGULO_BASE_SPRITE = anguloBaseSprite;
        this.POSICION = new Vector2(x, y);
        this.LIMITES = new Rectangle(x, y, ConfiguracionJuego.TAMANO_TANQUE, ConfiguracionJuego.TAMANO_TANQUE);
        this.angulo = anguloInicial;
        this.vidas = ConfiguracionJuego.VIDAS_MAXIMAS;
    }

    public void avanzar(float delta, Arena arena, Tanque otro) {
        mover(1f, delta, arena, otro);
    }

    public void retroceder(float delta, Arena arena, Tanque otro) {
        mover(-1f, delta, arena, otro);
    }

    public void rotar(float sentido, float delta) {
        angulo += sentido * ConfiguracionJuego.VELOCIDAD_ROTACION * delta;
        angulo = ((angulo % 360f) + 360f) % 360f;
    }

    private void mover(float signo, float delta, Arena arena, Tanque otro) {
        float distancia = ConfiguracionJuego.VELOCIDAD_TANQUE * delta * signo;
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

    private boolean estaLibre(Rectangle intento, Arena arena, Tanque otro) {
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

    public void iniciarExplosion() {
        tiempoExplosion = 0f;
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
        float origen = LIMITES.width / 2f;
        float rotacion = angulo - ANGULO_BASE_SPRITE;
        lote.draw(TEXTURA_NORMAL, POSICION.x, POSICION.y, origen, origen,
            LIMITES.width, LIMITES.height, 1f, 1f, rotacion,
            0, 0, TEXTURA_NORMAL.getWidth(), TEXTURA_NORMAL.getHeight(), false, false);
    }

    private void dibujarExplosionOCarcasa(SpriteBatch lote) {
        if (!ANIMACION_EXPLOSION.isAnimationFinished(tiempoExplosion)) {
            TextureRegion frame = ANIMACION_EXPLOSION.getKeyFrame(tiempoExplosion, false);
            float offsetX = (frame.getRegionWidth() - LIMITES.width) / 2f;
            float offsetY = (frame.getRegionHeight() - LIMITES.height) / 2f;
            lote.draw(frame, POSICION.x - offsetX, POSICION.y - offsetY,
                frame.getRegionWidth(), frame.getRegionHeight());
        } else {
            lote.draw(TEXTURA_DESTRUIDO, POSICION.x, POSICION.y, LIMITES.width, LIMITES.height);
        }
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

    public float obtenerAngulo() {
        return angulo;
    }
}
