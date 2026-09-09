package com.mistacorp.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mistacorp.game.screens.MenuScreen;

/**
 * Punto de entrada de la aplicación, compartido por todas las plataformas
 * (por ahora, escritorio vía LWJGL3). Extiende {@link Game} para delegar el
 * ciclo de vida en pantallas ({@link com.badlogic.gdx.Screen}) en lugar de
 * manejar todo en una única clase.
 * <p>
 * También administra los recursos "caros" que conviene crear una sola vez
 * y compartir entre pantallas: el {@link SpriteBatch}, la fuente por
 * defecto y una textura de 1x1 blanca que se tiñe por color para dibujar
 * los placeholders de tanques, paredes y proyectiles sin depender de
 * archivos de imagen externos.
 */
public class KatapumMain extends Game {

    private SpriteBatch batch;
    private BitmapFont font;
    private Texture pixel;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        pixel = createPixelTexture();

        setScreen(new MenuScreen(this));
    }

    private Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Texture getPixel() {
        return pixel;
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        batch.dispose();
        font.dispose();
        pixel.dispose();
    }
}
