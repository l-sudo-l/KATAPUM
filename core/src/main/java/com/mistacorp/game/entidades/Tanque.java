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

    private final String nombre;
    private final Vector2 posicion;
    private final Rectangle limites;
    private float angulo;
    private int vidas;

    private final Texture texturaNormal;
    private final Texture texturaDestruido;
    private final Animation<TextureRegion> animacionExplosion;
    private final float anguloBaseSprite;
    private float tiempoExplosion = -1f;

    public Tanque(String nombre, Texture texturaNormal, Texture texturaDestruido,
                  Animation<TextureRegion> animacionExplosion, float anguloBaseSprite,
                  float x, float y, float anguloInicial) {
        this.nombre = nombre;
        this.texturaNormal = texturaNormal;
        this.texturaDestruido = texturaDestruido;
        this.animacionExplosion = animacionExplosion;
        this.anguloBaseSprite = anguloBaseSprite;
        this.posicion = new Vector2(x, y);
        this.limites = new Rectangle(x, y, ConfiguracionJuego.TAMANO_TANQUE, ConfiguracionJuego.TAMANO_TANQUE);
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

        float nuevoX = posicion.x + dx;
        Rectangle intentoX = new Rectangle(nuevoX, posicion.y, limites.width, limites.height);
        if (estaLibre(intentoX, arena, otro)) {
            posicion.x = nuevoX;
        }

        float nuevoY = posicion.y + dy;
        Rectangle intentoY = new Rectangle(posicion.x, nuevoY, limites.width, limites.height);
        if (estaLibre(intentoY, arena, otro)) {
            posicion.y = nuevoY;
        }

        limites.setPosition(posicion.x, posicion.y);
    }

    private boolean estaLibre(Rectangle intento, Arena arena, Tanque otro) {
        return arena.estaDentroDeLimites(intento)
            && !arena.colisionaConObstaculos(intento)
            && !intento.overlaps(otro.limites);
    }

    public Vector2 obtenerPosicionCanon() {
        float centroX = posicion.x + limites.width / 2f;
        float centroY = posicion.y + limites.height / 2f;
        float desplazamiento = limites.width / 2f + 4f;
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
        return tiempoExplosion >= 0 && animacionExplosion.isAnimationFinished(tiempoExplosion);
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
        float origen = limites.width / 2f;
        float rotacion = angulo - anguloBaseSprite;
        lote.draw(texturaNormal, posicion.x, posicion.y, origen, origen,
            limites.width, limites.height, 1f, 1f, rotacion,
            0, 0, texturaNormal.getWidth(), texturaNormal.getHeight(), false, false);
    }

    private void dibujarExplosionOCarcasa(SpriteBatch lote) {
        if (!animacionExplosion.isAnimationFinished(tiempoExplosion)) {
            TextureRegion frame = animacionExplosion.getKeyFrame(tiempoExplosion, false);
            float offsetX = (frame.getRegionWidth() - limites.width) / 2f;
            float offsetY = (frame.getRegionHeight() - limites.height) / 2f;
            lote.draw(frame, posicion.x - offsetX, posicion.y - offsetY,
                frame.getRegionWidth(), frame.getRegionHeight());
        } else {
            lote.draw(texturaDestruido, posicion.x, posicion.y, limites.width, limites.height);
        }
    }

    public String obtenerNombre() {
        return nombre;
    }

    public int obtenerVidas() {
        return vidas;
    }

    public Rectangle obtenerLimites() {
        return limites;
    }

    public float obtenerAngulo() {
        return angulo;
    }
}
