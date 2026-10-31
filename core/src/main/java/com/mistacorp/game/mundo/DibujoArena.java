package com.mistacorp.game.mundo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.mundo.Arena;
import com.mistacorp.game.compartido.mundo.Decoracion;
import com.mistacorp.game.compartido.mundo.Pared;

public class DibujoArena {

    private static final int TAMANO_TILE = 32;

    private final RecursosGraficos RECURSOS;

    public DibujoArena(RecursosGraficos recursos) {
        this.RECURSOS = recursos;
    }

    public void dibujar(SpriteBatch lote, Arena arena) {
        Rectangle mundo = arena.obtenerLimitesMundo();
        dibujarTileado(lote, RECURSOS.obtenerRegionSuelo(), 0, 0, mundo.width, mundo.height);

        for (Pared pared : arena.obtenerParedes()) {
            Rectangle b = pared.obtenerLimites();
            dibujarTileado(lote, regionDePared(pared), b.x, b.y, b.width, b.height);
        }

        for (Decoracion decoracion : arena.obtenerDecoraciones()) {
            Rectangle b = decoracion.obtenerLimites();
            lote.draw(regionDeDecoracion(decoracion), b.x, b.y, b.width, b.height);
        }
    }

    private TextureRegion regionDePared(Pared pared) {
        if (pared.estaDestruido()) {
            return RECURSOS.obtenerRegionParedDestruida();
        }
        return RECURSOS.obtenerFramesDestruccionPared()[pared.obtenerImpactosRecibidos()];
    }

    private TextureRegion regionDeDecoracion(Decoracion decoracion) {
        switch (decoracion.obtenerTipo()) {
            case CAJA:
                if (decoracion.estaDestruido()) {
                    return RECURSOS.obtenerRegionCajaDestruida();
                }
                return RECURSOS.obtenerFramesDestruccionCaja()[decoracion.obtenerImpactosRecibidos()];
            case BARRIL:
                if (decoracion.estaDestruido()) {
                    return RECURSOS.obtenerRegionBarrilDestruido();
                }
                return RECURSOS.obtenerFramesDestruccionBarril()[decoracion.obtenerImpactosRecibidos()];
            default:
                return RECURSOS.obtenerRegionArbusto();
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
