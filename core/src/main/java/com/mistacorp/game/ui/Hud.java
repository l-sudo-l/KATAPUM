package com.mistacorp.game.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.entities.Tank;

/**
 * Interfaz fija en pantalla, separada visualmente del entorno de juego.
 * Usa su propio {@link ScreenViewport} (independiente de la cámara del
 * mundo), por lo que el tamaño del texto no se ve afectado por el zoom o
 * el desplazamiento de la cámara de juego.
 * <p>
 * Por ahora muestra la información mínima relevante para este género:
 * las vidas restantes de cada jugador y el estado de pausa.
 */
public class Hud {

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new ScreenViewport(camera);
    private final BitmapFont font;

    public Hud(BitmapFont font) {
        this.font = font;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(SpriteBatch batch, Tank player1, Tank player2, boolean paused) {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, livesLabel(player1), 16, viewport.getWorldHeight() - 16);
        font.draw(batch, livesLabel(player2), viewport.getWorldWidth() - 220, viewport.getWorldHeight() - 16);

        if (paused) {
            font.draw(batch, "PAUSA - ESC para continuar",
                viewport.getWorldWidth() / 2f - 110, viewport.getWorldHeight() / 2f);
        }

        batch.end();
    }

    private String livesLabel(Tank tank) {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < tank.getLives(); i++) {
            hearts.append("<3 ");
        }
        return tank.getName() + "  " + hearts.toString().trim();
    }
}
