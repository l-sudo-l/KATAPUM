package com.mistacorp.game.interfaz;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.mundo.SpritesMapa;

public class SelectorMapa {

    private static final float ANCHO_MARCO = 248f;
    private static final float BORDE_MARCO = 4f;
    private static final float TAMANO_FLECHA = 32f;
    private static final float SEPARACION_FLECHAS = 60f;

    private final float X;
    private final float Y;
    private final Rectangle FLECHA_ANTERIOR;
    private final Rectangle FLECHA_SIGUIENTE;
    private int indiceMapa = 0;

    public SelectorMapa(float x, float y) {
        this.X = x;
        this.Y = y;
        FLECHA_ANTERIOR = new Rectangle(x, y - SEPARACION_FLECHAS, TAMANO_FLECHA, TAMANO_FLECHA);
        FLECHA_SIGUIENTE = new Rectangle(x + ANCHO_MARCO - TAMANO_FLECHA, y - SEPARACION_FLECHAS, TAMANO_FLECHA, TAMANO_FLECHA);
    }

    public boolean manejarClick(float mouseX, float mouseY) {
        if (FLECHA_ANTERIOR.contains(mouseX, mouseY)) {
            cambiar(-1);
            return true;
        }
        if (FLECHA_SIGUIENTE.contains(mouseX, mouseY)) {
            cambiar(1);
            return true;
        }
        return false;
    }

    public void cambiar(int cambio) {
        int cantidad = TipoMapa.values().length;
        indiceMapa = (indiceMapa + cambio + cantidad) % cantidad;
    }

    public TipoMapa obtenerMapa() {
        return TipoMapa.values()[indiceMapa];
    }

    public void dibujar(SpriteBatch lote, RecursosGraficos recursos, BitmapFont fuente) {
        SpritesMapa sprites = recursos.obtenerSpritesMapa(obtenerMapa());
        lote.draw(sprites.obtenerTexturaMarcoSeleccionado(), X, Y);
        lote.draw(sprites.obtenerTexturaMiniatura(), X + BORDE_MARCO, Y + BORDE_MARCO);
        lote.draw(recursos.obtenerTexturaFlechaIzquierda(), FLECHA_ANTERIOR.x, FLECHA_ANTERIOR.y, TAMANO_FLECHA, TAMANO_FLECHA);
        lote.draw(recursos.obtenerTexturaFlechaDerecha(), FLECHA_SIGUIENTE.x, FLECHA_SIGUIENTE.y, TAMANO_FLECHA, TAMANO_FLECHA);

        String nombre = obtenerMapa().obtenerNombreVisible();
        GlyphLayout layout = new GlyphLayout(fuente, nombre);
        fuente.draw(lote, nombre, X + ANCHO_MARCO / 2f - layout.width / 2f, FLECHA_ANTERIOR.y + 22f);
    }
}
