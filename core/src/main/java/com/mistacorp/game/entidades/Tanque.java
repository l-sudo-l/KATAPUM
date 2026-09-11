package com.mistacorp.game.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.mundo.Arena;

public class Tanque {

    public enum Direccion {
        ARRIBA(0, 1), ABAJO(0, -1), IZQUIERDA(-1, 0), DERECHA(1, 0);

        public final float dx;
        public final float dy;

        Direccion(float dx, float dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private final String nombre;
    private final Color color;
    private final Vector2 posicion;
    private final Rectangle limites;
    private Direccion direccion;
    private int vidas;

    public Tanque(String nombre, Color color, float x, float y, Direccion direccion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = new Vector2(x, y);
        this.limites = new Rectangle(x, y, ConfiguracionJuego.TAMANO_TANQUE, ConfiguracionJuego.TAMANO_TANQUE);
        this.direccion = direccion;
        this.vidas = ConfiguracionJuego.VIDAS_MAXIMAS;
    }

    public void intentarMover(float dx, float dy, float delta, Arena arena, Tanque otro) {
        if (dx == 0 && dy == 0) {
            return;
        }

        Vector2 direccionMovimiento = new Vector2(dx, dy).nor();
        float distancia = ConfiguracionJuego.VELOCIDAD_TANQUE * delta;

        float nuevoX = posicion.x + direccionMovimiento.x * distancia;
        Rectangle intentoX = new Rectangle(nuevoX, posicion.y, limites.width, limites.height);
        if (estaLibre(intentoX, arena, otro)) {
            posicion.x = nuevoX;
        }

        float nuevoY = posicion.y + direccionMovimiento.y * distancia;
        Rectangle intentoY = new Rectangle(posicion.x, nuevoY, limites.width, limites.height);
        if (estaLibre(intentoY, arena, otro)) {
            posicion.y = nuevoY;
        }

        limites.setPosition(posicion.x, posicion.y);
        direccion = direccionDesde(dx, dy, direccion);
    }

    private boolean estaLibre(Rectangle intento, Arena arena, Tanque otro) {
        return arena.estaDentroDeLimites(intento)
            && !arena.colisionaConParedes(intento)
            && !intento.overlaps(otro.limites);
    }

    private static Direccion direccionDesde(float dx, float dy, Direccion actual) {
        if (dy > 0) return Direccion.ARRIBA;
        if (dy < 0) return Direccion.ABAJO;
        if (dx > 0) return Direccion.DERECHA;
        if (dx < 0) return Direccion.IZQUIERDA;
        return actual;
    }

    public Vector2 obtenerPosicionCanon() {
        float centroX = posicion.x + limites.width / 2f;
        float centroY = posicion.y + limites.height / 2f;
        float desplazamiento = limites.width / 2f + 4f;
        return new Vector2(centroX + direccion.dx * desplazamiento, centroY + direccion.dy * desplazamiento);
    }

    public void recibirImpacto() {
        vidas = Math.max(0, vidas - 1);
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public void dibujar(SpriteBatch lote, Texture pixel) {
        lote.setColor(color);
        lote.draw(pixel, posicion.x, posicion.y, limites.width, limites.height);
        lote.setColor(Color.WHITE);
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

    public Direccion obtenerDireccion() {
        return direccion;
    }
}
