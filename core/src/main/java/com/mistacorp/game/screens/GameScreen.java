package com.mistacorp.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.GameConfig;
import com.mistacorp.game.KatapumMain;
import com.mistacorp.game.entities.Projectile;
import com.mistacorp.game.entities.Tank;
import com.mistacorp.game.input.PlayerInputHandler;
import com.mistacorp.game.ui.Hud;
import com.mistacorp.game.world.Arena;

/**
 * Pantalla principal de juego. Es la clase "orquestadora": no implementa
 * reglas de bajo nivel ella misma (eso vive en Tank, Projectile y Arena),
 * sino que en cada frame consulta el input de cada jugador, le pide a cada
 * tanque que intente moverse/disparar, actualiza los proyectiles y resuelve
 * la condición de victoria.
 * <p>
 * Mecánica central del prototipo: combate de tanques en modo local
 * (hotseat), con vidas, colisión contra obstáculos y contra el tanque
 * rival, y una pantalla de fin de partida al quedar un único sobreviviente.
 */
public class GameScreen extends ScreenAdapter {

    private final KatapumMain game;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

    private final Arena arena = new Arena();
    private final Tank player1;
    private final Tank player2;
    private final Array<Projectile> projectiles = new Array<>();

    private final PlayerInputHandler input1;
    private final PlayerInputHandler input2;
    private final Hud hud;

    private boolean paused = false;

    public GameScreen(KatapumMain game) {
        this.game = game;

        float spawnY = GameConfig.WORLD_HEIGHT / 2f - GameConfig.TANK_SIZE / 2f;
        player1 = new Tank("Jugador 1", GameConfig.PLAYER1_COLOR, 60, spawnY, Tank.Direction.RIGHT);
        player2 = new Tank("Jugador 2", GameConfig.PLAYER2_COLOR,
            GameConfig.WORLD_WIDTH - 60 - GameConfig.TANK_SIZE, spawnY, Tank.Direction.LEFT);

        input1 = new PlayerInputHandler(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
        input2 = new PlayerInputHandler(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Input.Keys.CONTROL_RIGHT);

        hud = new Hud(game.getFont());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(input1, input2));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        if (!paused) {
            update(delta);
        }

        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        arena.render(game.getBatch(), game.getPixel());
        player1.render(game.getBatch(), game.getPixel());
        player2.render(game.getBatch(), game.getPixel());
        for (Projectile projectile : projectiles) {
            projectile.render(game.getBatch(), game.getPixel());
        }

        game.getBatch().end();

        hud.render(game.getBatch(), player1, player2, paused);
    }

    private void update(float delta) {
        handlePlayer(delta, input1, player1, player2);
        handlePlayer(delta, input2, player2, player1);
        updateProjectiles(delta);
    }

    private void handlePlayer(float delta, PlayerInputHandler input, Tank tank, Tank other) {
        tank.tryMove(input.getMoveX(), input.getMoveY(), delta, arena, other);
        if (input.consumeShoot()) {
            projectiles.add(new Projectile(tank.getMuzzlePosition(), tank.getFacing(), tank));
        }
    }

    private void updateProjectiles(float delta) {
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);

            boolean shouldRemove = false;

            if (!arena.isInsideBounds(projectile.getBounds()) || arena.collidesWithWalls(projectile.getBounds())) {
                shouldRemove = true;
            } else {
                Tank target = projectile.getOwner() == player1 ? player2 : player1;
                if (projectile.getBounds().overlaps(target.getBounds())) {
                    target.hit();
                    shouldRemove = true;
                    if (!target.isAlive()) {
                        game.setScreen(new GameOverScreen(game, projectile.getOwner().getName()));
                        return;
                    }
                }
            }

            if (shouldRemove) {
                projectiles.removeIndex(i);
            }
        }
    }
}
