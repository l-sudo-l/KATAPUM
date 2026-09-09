package com.mistacorp.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.GameConfig;
import com.mistacorp.game.KatapumMain;

/**
 * Pantalla de fin de partida: informa quién ganó y permite volver al menú.
 * Junto con {@link MenuScreen} y {@link GameScreen} cubre el mínimo de tres
 * pantallas/estados pedido en la consigna (inicial, juego, y un estado
 * adicional — en este caso, finalización).
 */
public class GameOverScreen extends ScreenAdapter {

    private final KatapumMain game;
    private final String winnerName;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

    public GameOverScreen(KatapumMain game, String winnerName) {
        this.game = game;
        this.winnerName = winnerName;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    game.setScreen(new MenuScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.08f, 1f);

        viewport.apply();
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getFont().draw(game.getBatch(), "GANADOR: " + winnerName,
            GameConfig.WORLD_WIDTH / 2f - 150, GameConfig.WORLD_HEIGHT / 2f + 20);
        game.getFont().draw(game.getBatch(), "Presiona ENTER para volver al menu",
            GameConfig.WORLD_WIDTH / 2f - 190, GameConfig.WORLD_HEIGHT / 2f - 20);

        game.getBatch().end();
    }
}
