package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Efecto {

    private static final float DURACION = 0.12f;

    private final Texture TEXTURA;
    private final float X;
    private final float Y;
    private final float ANCHO;
    private final float ALTO;
    private float tiempoTranscurrido = 0f;

    public Efecto(Texture textura, Vector2 centro, float ancho, float alto) {
        this.TEXTURA = textura;
        this.X = centro.x - ancho / 2f;
        this.Y = centro.y - alto / 2f;
        this.ANCHO = ancho;
        this.ALTO = alto;
    }

    public void actualizar(float delta) {
        tiempoTranscurrido += delta;
    }

    public boolean estaFinalizado() {
        return tiempoTranscurrido >= DURACION;
    }

    public void dibujar(SpriteBatch lote) {
        lote.draw(TEXTURA, X, Y, ANCHO, ALTO);
    }
}
