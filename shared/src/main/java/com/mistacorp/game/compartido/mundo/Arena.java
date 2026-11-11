package com.mistacorp.game.compartido.mundo;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.TipoMapa;

public class Arena {

    private static final int TAMANO_TILE = 32;

    private final TipoMapa MAPA;
    private final Array<Pared> PAREDES = new Array<>();
    private final Array<Decoracion> DECORACIONES = new Array<>();
    private final Rectangle LIMITES_MUNDO;

    public Arena(TipoMapa mapa) {
        this.MAPA = mapa;
        this.LIMITES_MUNDO = new Rectangle(0, 0, ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO);
        construirDesdeGrilla(DisenoMapa.obtenerGrilla(mapa));
    }

    private void construirDesdeGrilla(String[] grilla) {
        for (int fila = 0; fila < grilla.length; fila++) {
            float y = Math.max(0, ConfiguracionPartida.ALTO_MUNDO - (fila + 1) * TAMANO_TILE);
            for (int columna = 0; columna < grilla[fila].length(); columna++) {
                float x = columna * TAMANO_TILE;
                agregarObjeto(grilla[fila].charAt(columna), x, y);
            }
        }
    }

    private void agregarObjeto(char simbolo, float x, float y) {
        switch (simbolo) {
            case DisenoMapa.PARED:
                PAREDES.add(new Pared(x, y, TAMANO_TILE, TAMANO_TILE));
                break;
            case DisenoMapa.VEGETACION:
                DECORACIONES.add(new Decoracion(x, y, TipoDecoracion.VEGETACION));
                break;
            case DisenoMapa.CAJA:
                DECORACIONES.add(new Decoracion(x, y, TipoDecoracion.CAJA));
                break;
            case DisenoMapa.BARRIL:
                DECORACIONES.add(new Decoracion(x, y, TipoDecoracion.BARRIL));
                break;
            default:
                break;
        }
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
            if (!pared.estaDestruido() && pared.obtenerLimitesColision().overlaps(limites)) {
                return pared;
            }
        }
        for (Decoracion decoracion : DECORACIONES) {
            if (!decoracion.estaDestruido() && decoracion.obtenerLimitesColision().overlaps(limites)) {
                return decoracion;
            }
        }
        return null;
    }

    public TipoMapa obtenerMapa() {
        return MAPA;
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
