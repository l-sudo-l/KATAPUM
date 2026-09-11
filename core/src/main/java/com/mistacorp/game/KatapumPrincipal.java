package com.mistacorp.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mistacorp.game.pantallas.PantallaMenu;

public class KatapumPrincipal extends Game {

    private SpriteBatch lote;
    private BitmapFont fuente;
    private Texture pixel;

    @Override
    public void create() {
        lote = new SpriteBatch();
        fuente = new BitmapFont();
        fuente.getData().setScale(1.2f);
        pixel = crearTexturaPixel();

        setScreen(new PantallaMenu(this));
    }

    private Texture crearTexturaPixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return textura;
    }

    public SpriteBatch obtenerLote() {
        return lote;
    }

    public BitmapFont obtenerFuente() {
        return fuente;
    }

    public Texture obtenerPixel() {
        return pixel;
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        lote.dispose();
        fuente.dispose();
        pixel.dispose();
    }
}
