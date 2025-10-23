package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * A spawner that creates Magpie entities periodically.
 */
public class MagpieSpawner implements Spawner {

    private int x0 = 0;
    private int y0 = 0;
    private TickTimer timer;

    /**
     * Constructs a MagpieSpawner with default duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public MagpieSpawner(int x, int y) {
        this.x0 = x;
        this.y0 = y;
        this.timer = new RepeatingTimer(360);
    }

    /**
     * Constructs a MagpieSpawner with custom duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the timer duration
     */
    public MagpieSpawner(int x, int y, int duration) {
        this.x0 = x;
        this.y0 = y;
        this.timer = new RepeatingTimer(duration);
    }

    @Override
    public TickTimer getTimer() {
        return timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.timer.tick();
        if (this.getTimer().isFinished()) {
            game.getEnemies().spawnX = this.getX();
            game.getEnemies().spawnY = this.getY();
            game.getEnemies().mkM(game.getPlayer());
        }
    }

    @Override
    public int getX() {
        return this.x0;
    }

    @Override
    public void setX(int x) {
        this.x0 = x;
    }

    @Override
    public int getY() {
        return this.y0;
    }

    @Override
    public void setY(int y) {
        this.y0 = y;
    }
}
