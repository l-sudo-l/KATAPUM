package com.mistacorp.game.compartido.mundo;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.compartido.ConfiguracionPartida;

public class Arena {

    private final Array<Pared> PAREDES = new Array<>();
    private final Array<Decoracion> DECORACIONES = new Array<>();
    private final Rectangle LIMITES_MUNDO;

    public Arena() {
        this.LIMITES_MUNDO = new Rectangle(0, 0, ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO);
        construirDisposicion();
        construirDecoraciones();
    }

    private void construirDisposicion() {
        PAREDES.add(new Pared(220, 380, 96, 32));
        PAREDES.add(new Pared(640, 380, 96, 32));
        PAREDES.add(new Pared(150, 250, 32, 96));
        PAREDES.add(new Pared(778, 250, 32, 96));
        PAREDES.add(new Pared(432, 290, 96, 32));
        PAREDES.add(new Pared(150, 120, 96, 32));
        PAREDES.add(new Pared(714, 120, 96, 32));
        PAREDES.add(new Pared(432, 70, 32, 96));
    }

    private void construirDecoraciones() {
        DECORACIONES.add(new Decoracion(60, 420, TipoDecoracion.ARBUSTO));
        DECORACIONES.add(new Decoracion(860, 420, TipoDecoracion.ARBUSTO));
        DECORACIONES.add(new Decoracion(60, 90, TipoDecoracion.ARBUSTO));
        DECORACIONES.add(new Decoracion(480, 460, TipoDecoracion.CAJA));
        DECORACIONES.add(new Decoracion(480, 200, TipoDecoracion.CAJA));
        DECORACIONES.add(new Decoracion(300, 460, TipoDecoracion.BARRIL));
        DECORACIONES.add(new Decoracion(660, 90, TipoDecoracion.BARRIL));
    }

    public boolean estaDentroDeLimites(Rectangle limites) {
        return LIMITES_MUNDO.contains(limites);
    }

    public boolean colisionaConObstaculos(Rectangle limites) {
        return buscarObstaculoEn(limites) != null;
    }

    public boolean impactarObstaculo(Rectangle limites) {
        Obstaculo obstaculo = buscarObstaculoEn(limites);
        if (obstaculo == null) {
            return false;
        }
        obstaculo.recibirImpacto();
        return true;
    }

    private Obstaculo buscarObstaculoEn(Rectangle limites) {
        for (Pared pared : PAREDES) {
            if (!pared.estaDestruido() && pared.obtenerLimites().overlaps(limites)) {
                return pared;
            }
        }
        for (Decoracion decoracion : DECORACIONES) {
            if (!decoracion.estaDestruido() && decoracion.obtenerLimites().overlaps(limites)) {
                return decoracion;
            }
        }
        return null;
    }

    public Array<Pared> obtenerParedes() {
        return PAREDES;
    }

    public Array<Decoracion> obtenerDecoraciones() {
        return DECORACIONES;
    }

    public Rectangle obtenerLimitesMundo() {
        return LIMITES_MUNDO;
    }
}
