package com.mistacorp.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.GameConfig;

/**
 * Representa el entorno visual y de reglas del juego: los límites del mundo
 * y los obstáculos fijos que actúan como cobertura táctica. Es la clase
 * responsable de responder "¿esta posición es válida?" tanto para los
 * tanques como para los proyectiles, evitando duplicar la lógica de
 * colisión contra paredes en cada entidad.
 */
public class Arena {

    private final Array<Wall> walls = new Array<>();
    private final Rectangle worldBounds;

    public Arena() {
        this.worldBounds = new Rectangle(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        buildLayout();
    }

    /**
     * Distribución de paredes inspirada en el boceto de la propuesta original:
     * bloques simétricos que dejan pasillos y zonas de cobertura entre ambos
     * puntos de aparición.
     */
    private void buildLayout() {
        walls.add(new Wall(220, 380, 96, 32));
        walls.add(new Wall(640, 380, 96, 32));
        walls.add(new Wall(150, 250, 32, 96));
        walls.add(new Wall(778, 250, 32, 96));
        walls.add(new Wall(432, 290, 96, 32));
        walls.add(new Wall(150, 120, 96, 32));
        walls.add(new Wall(714, 120, 96, 32));
        walls.add(new Wall(432, 70, 32, 96));
    }

    public boolean isInsideBounds(Rectangle bounds) {
        return worldBounds.contains(bounds);
    }

    public boolean collidesWithWalls(Rectangle bounds) {
        for (Wall wall : walls) {
            if (wall.getBounds().overlaps(bounds)) {
                return true;
            }
        }
        return false;
    }

    public void render(SpriteBatch batch, Texture pixel) {
        batch.setColor(GameConfig.GROUND_COLOR);
        batch.draw(pixel, 0, 0, worldBounds.width, worldBounds.height);

        batch.setColor(GameConfig.WALL_COLOR);
        for (Wall wall : walls) {
            Rectangle b = wall.getBounds();
            batch.draw(pixel, b.x, b.y, b.width, b.height);
        }
        batch.setColor(Color.WHITE);
    }
}
