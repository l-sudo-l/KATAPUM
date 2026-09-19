package com.mistacorp.game.mundo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.RecursosGraficos;

public class Arena {

    private enum TipoDecoracion {
        ARBUSTO, CAJA, BARRIL
    }

    private static class Decoracion {
        final Rectangle limites;
        final TipoDecoracion tipo;

        Decoracion(float x, float y, TipoDecoracion tipo) {
            this.limites = new Rectangle(x, y, TAMANO_TILE, TAMANO_TILE);
            this.tipo = tipo;
        }
    }

    private static final int TAMANO_TILE = 32;

    private final Array<Pared> paredes = new Array<>();
    private final Array<Decoracion> decoraciones = new Array<>();
    private final Rectangle limitesMundo;

    public Arena() {
        this.limitesMundo = new Rectangle(0, 0, ConfiguracionJuego.ANCHO_MUNDO, ConfiguracionJuego.ALTO_MUNDO);
        construirDisposicion();
        construirDecoraciones();
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

    private void construirDecoraciones() {
        decoraciones.add(new Decoracion(60, 420, TipoDecoracion.ARBUSTO));
        decoraciones.add(new Decoracion(860, 420, TipoDecoracion.ARBUSTO));
        decoraciones.add(new Decoracion(60, 90, TipoDecoracion.ARBUSTO));
        decoraciones.add(new Decoracion(480, 460, TipoDecoracion.CAJA));
        decoraciones.add(new Decoracion(480, 200, TipoDecoracion.CAJA));
        decoraciones.add(new Decoracion(300, 460, TipoDecoracion.BARRIL));
        decoraciones.add(new Decoracion(660, 90, TipoDecoracion.BARRIL));
    }

    public boolean estaDentroDeLimites(Rectangle limites) {
        return limitesMundo.contains(limites);
    }

    public boolean colisionaConObstaculos(Rectangle limites) {
        for (Pared pared : paredes) {
            if (pared.obtenerLimites().overlaps(limites)) {
                return true;
            }
        }
        for (Decoracion decoracion : decoraciones) {
            if (decoracion.limites.overlaps(limites)) {
                return true;
            }
        }
        return false;
    }

    public void dibujar(SpriteBatch lote, RecursosGraficos recursos) {
        dibujarTileado(lote, recursos.obtenerTexturaSuelo(), 0, 0, limitesMundo.width, limitesMundo.height);

        for (Pared pared : paredes) {
            Rectangle b = pared.obtenerLimites();
            dibujarTileado(lote, recursos.obtenerTexturaPared(), b.x, b.y, b.width, b.height);
        }

        for (Decoracion decoracion : decoraciones) {
            Texture textura = texturaDeDecoracion(recursos, decoracion.tipo);
            lote.draw(textura, decoracion.limites.x, decoracion.limites.y, TAMANO_TILE, TAMANO_TILE);
        }
    }

    private Texture texturaDeDecoracion(RecursosGraficos recursos, TipoDecoracion tipo) {
        switch (tipo) {
            case CAJA:
                return recursos.obtenerTexturaCaja();
            case BARRIL:
                return recursos.obtenerTexturaBarril();
            default:
                return recursos.obtenerTexturaArbusto();
        }
    }

    private void dibujarTileado(SpriteBatch lote, Texture textura, float x, float y, float ancho, float alto) {
        int columnas = (int) Math.ceil(ancho / TAMANO_TILE);
        int filas = (int) Math.ceil(alto / TAMANO_TILE);

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                float tileX = x + columna * TAMANO_TILE;
                float tileY = y + fila * TAMANO_TILE;
                float anchoTile = Math.min(TAMANO_TILE, x + ancho - tileX);
                float altoTile = Math.min(TAMANO_TILE, y + alto - tileY);
                lote.draw(textura, tileX, tileY, anchoTile, altoTile);
            }
        }
    }
}
