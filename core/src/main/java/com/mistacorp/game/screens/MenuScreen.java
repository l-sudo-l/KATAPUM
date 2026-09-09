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
 * Pantalla inicial del juego. Muestra el título, un resumen de los
 * controles de ambos jugadores y espera la confirmación (ENTER) para
 * comenzar una partida local.
 */
public class MenuScreen extends ScreenAdapter {

    private final KatapumMain game;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

    public MenuScreen(KatapumMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    game.setScreen(new GameScreen(game));
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
        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);

        viewport.apply();
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getFont().draw(game.getBatch(), "KATAPUM",
            GameConfig.WORLD_WIDTH / 2f - 60, GameConfig.WORLD_HEIGHT - 80);
        game.getFont().draw(game.getBatch(), "Presiona ENTER para jugar",
            GameConfig.WORLD_WIDTH / 2f - 130, GameConfig.WORLD_HEIGHT / 2f + 40);
        game.getFont().draw(game.getBatch(), "Jugador 1: W A S D + ESPACIO",
            GameConfig.WORLD_WIDTH / 2f - 150, GameConfig.WORLD_HEIGHT / 2f - 10);
        game.getFont().draw(game.getBatch(), "Jugador 2: Flechas + CTRL derecho",
            GameConfig.WORLD_WIDTH / 2f - 150, GameConfig.WORLD_HEIGHT / 2f - 40);

        game.getBatch().end();
    }
}
