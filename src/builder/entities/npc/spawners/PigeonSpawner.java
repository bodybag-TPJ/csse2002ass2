package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

import java.util.List;

/**
 * A spawner that creates Pigeon entities when cabbages are present.
 */
public class PigeonSpawner implements Spawner {

    private int xPos = 0;
    private int yPos = 0;
    private final RepeatingTimer timer;

    /**
     * Constructs a PigeonSpawner with default duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public PigeonSpawner(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.timer = new RepeatingTimer(100);
    }

    /**
     * Constructs a PigeonSpawner with custom duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the timer duration
     */
    public PigeonSpawner(int x, int y, int duration) {
        this.xPos = x;
        this.yPos = y;
        this.timer = new RepeatingTimer(duration);
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.timer.tick();

        List<Tile> tiles =
                game.getWorld()
                        .tileSelector(
                                tile -> {
                                    for (Entity entity : tile.getStackedEntities()) {
                                        if (entity instanceof Cabbage) {
                                            return true;
                                        }
                                    }
                                    return false;
                                });


        if (tiles.size() > 0) {
            int distance = this.distanceFrom(tiles.getFirst());
            Tile closest = tiles.getFirst();
            for (Tile tile : tiles) {
                if (this.distanceFrom(tile) < distance) {
                    closest = tile;
                }
            }

            if (this.getTimer().isFinished()) {
                game.getEnemies().spawnX = this.getX();
                game.getEnemies().spawnY = this.getY();
                game.getEnemies().mkP(closest);
            }
        }
    }

    /**
     * Return how far away this npc is from the given position
     *
     * @param position the position we are measuring to from this npcs position!
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public int getX() {
        return this.xPos;
    }

    @Override
    public void setX(int x) {
        this.xPos = x;
    }

    @Override
    public int getY() {
        return this.yPos;
    }

    @Override
    public void setY(int y) {
        this.yPos = y;
    }
}
