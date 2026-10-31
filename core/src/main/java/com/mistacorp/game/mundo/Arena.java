package com.mistacorp.game.mundo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.ConfiguracionPartida;

public class Arena {

    private static final int TAMANO_TILE = 32;

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

    public void dibujar(SpriteBatch lote, RecursosGraficos recursos) {
        dibujarTileado(lote, recursos.obtenerRegionSuelo(), 0, 0, LIMITES_MUNDO.width, LIMITES_MUNDO.height);

        for (Pared pared : PAREDES) {
            Rectangle b = pared.obtenerLimites();
            dibujarTileado(lote, regionDePared(recursos, pared), b.x, b.y, b.width, b.height);
        }

        for (Decoracion decoracion : DECORACIONES) {
            Rectangle b = decoracion.obtenerLimites();
            lote.draw(regionDeDecoracion(recursos, decoracion), b.x, b.y, b.width, b.height);
        }
    }

    private TextureRegion regionDePared(RecursosGraficos recursos, Pared pared) {
        if (pared.estaDestruido()) {
            return recursos.obtenerRegionParedDestruida();
        }
        return recursos.obtenerFramesDestruccionPared()[pared.obtenerImpactosRecibidos()];
    }

    private TextureRegion regionDeDecoracion(RecursosGraficos recursos, Decoracion decoracion) {
        switch (decoracion.obtenerTipo()) {
            case CAJA:
                if (decoracion.estaDestruido()) {
                    return recursos.obtenerRegionCajaDestruida();
                }
                return recursos.obtenerFramesDestruccionCaja()[decoracion.obtenerImpactosRecibidos()];
            case BARRIL:
                if (decoracion.estaDestruido()) {
                    return recursos.obtenerRegionBarrilDestruido();
                }
                return recursos.obtenerFramesDestruccionBarril()[decoracion.obtenerImpactosRecibidos()];
            default:
                return recursos.obtenerRegionArbusto();
        }
    }

    private void dibujarTileado(SpriteBatch lote, TextureRegion region, float x, float y, float ancho, float alto) {
        int columnas = (int) Math.ceil(ancho / TAMANO_TILE);
        int filas = (int) Math.ceil(alto / TAMANO_TILE);

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                float tileX = x + columna * TAMANO_TILE;
                float tileY = y + fila * TAMANO_TILE;
                float anchoTile = Math.min(TAMANO_TILE, x + ancho - tileX);
                float altoTile = Math.min(TAMANO_TILE, y + alto - tileY);
                lote.draw(region, tileX, tileY, anchoTile, altoTile);
            }
        }
    }
}
