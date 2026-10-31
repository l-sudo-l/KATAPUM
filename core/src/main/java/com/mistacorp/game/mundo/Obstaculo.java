package com.mistacorp.game.mundo;

import com.badlogic.gdx.math.Rectangle;

public class Obstaculo {

    private final Rectangle LIMITES;
    private final int IMPACTOS_PARA_DESTRUIR;
    private int impactosRecibidos = 0;

    public Obstaculo(float x, float y, float ancho, float alto, int impactosParaDestruir) {
        this.LIMITES = new Rectangle(x, y, ancho, alto);
        this.IMPACTOS_PARA_DESTRUIR = impactosParaDestruir;
    }

    public void recibirImpacto() {
        if (esDestructible() && !estaDestruido()) {
            impactosRecibidos++;
        }
    }

    public boolean esDestructible() {
        return IMPACTOS_PARA_DESTRUIR > 0;
    }

    public boolean estaDestruido() {
        return esDestructible() && impactosRecibidos >= IMPACTOS_PARA_DESTRUIR;
    }

    public int obtenerImpactosRecibidos() {
        return impactosRecibidos;
    }

    public Rectangle obtenerLimites() {
        return LIMITES;
    }
}
