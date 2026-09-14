package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Efecto {

    private static final float DURACION = 0.12f;

    private final Texture textura;
    private final float x;
    private final float y;
    private final float ancho;
    private final float alto;
    private float tiempoTranscurrido = 0f;

    public Efecto(Texture textura, Vector2 centro, float ancho, float alto) {
        this.textura = textura;
        this.x = centro.x - ancho / 2f;
        this.y = centro.y - alto / 2f;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void actualizar(float delta) {
        tiempoTranscurrido += delta;
    }

    public boolean estaFinalizado() {
        return tiempoTranscurrido >= DURACION;
    }

    public void dibujar(SpriteBatch lote) {
        lote.draw(textura, x, y, ancho, alto);
    }
}
