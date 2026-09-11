package com.mistacorp.game.mundo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.ConfiguracionJuego;

public class Arena {

    private final Array<Pared> paredes = new Array<>();
    private final Rectangle limitesMundo;

    public Arena() {
        this.limitesMundo = new Rectangle(0, 0, ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO);
        construirDisposicion();
    }

    private void construirDisposicion() {
        paredes.add(new Pared(220, 380, 96, 32));
        paredes.add(new Pared(640, 380, 96, 32));
        paredes.add(new Pared(150, 250, 32, 96));
        paredes.add(new Pared(778, 250, 32, 96));
        paredes.add(new Pared(432, 290, 96, 32));
        paredes.add(new Pared(150, 120, 96, 32));
        paredes.add(new Pared(714, 120, 96, 32));
        paredes.add(new Pared(432, 70, 32, 96));
    }

    public boolean estaDentroDeLimites(Rectangle limites) {
        return limitesMundo.contains(limites);
    }

    public boolean colisionaConParedes(Rectangle limites) {
        for (Pared pared : paredes) {
            if (pared.obtenerLimites().overlaps(limites)) {
                return true;
            }
        }
        return false;
    }

    public void dibujar(SpriteBatch lote, Texture pixel) {
        lote.setColor(ConfiguracionJuego.COLOR_SUELO);
        lote.draw(pixel, 0, 0, limitesMundo.width, limitesMundo.height);

        lote.setColor(ConfiguracionJuego.COLOR_PARED);
        for (Pared pared : paredes) {
            Rectangle b = pared.obtenerLimites();
            lote.draw(pixel, b.x, b.y, b.width, b.height);
        }
        lote.setColor(Color.WHITE);
    }
}
