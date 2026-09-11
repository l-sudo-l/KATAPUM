package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;

public class Proyectil {

    private final Vector2 posicion;
    private final Vector2 velocidad;
    private final Rectangle limites;
    private final Tanque propietario;

    public Proyectil(Vector2 posicionInicial, Tanque.Direccion direccion, Tanque propietario) {
        this.posicion = new Vector2(
            posicionInicial.x - ConfiguracionJuego.TAMANO_PROYECTIL / 2f,
            posicionInicial.y - ConfiguracionJuego.TAMANO_PROYECTIL / 2f
        );
        this.velocidad = new Vector2(direccion.dx, direccion.dy).scl(ConfiguracionJuego.VELOCIDAD_PROYECTIL);
        this.limites = new Rectangle(posicion.x, posicion.y, ConfiguracionJuego.TAMANO_PROYECTIL, ConfiguracionJuego.TAMANO_PROYECTIL);
        this.propietario = propietario;
    }

    public void actualizar(float delta) {
        posicion.mulAdd(velocidad, delta);
        limites.setPosition(posicion.x, posicion.y);
    }

    public void dibujar(SpriteBatch lote, Texture pixel) {
        lote.setColor(ConfiguracionJuego.COLOR_PROYECTIL);
        lote.draw(pixel, posicion.x, posicion.y, limites.width, limites.height);
        lote.setColor(Color.WHITE);
    }

    public Rectangle obtenerLimites() {
        return limites;
    }

    public Tanque obtenerPropietario() {
        return propietario;
    }
}
