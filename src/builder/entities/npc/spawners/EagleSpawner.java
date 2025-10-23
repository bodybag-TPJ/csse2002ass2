package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * A spawner that creates Eagle entities periodically.
 */
public class EagleSpawner implements Spawner {

    private int xPos = 0;
    private int yPos = 0;
    private TickTimer timer;

    /**
     * Constructs an EagleSpawner with default duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public EagleSpawner(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.timer = new RepeatingTimer(1000);
    }

    /**
     * Constructs an EagleSpawner with custom duration.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the timer duration
     */
    public EagleSpawner(int x, int y, int duration) {
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
        if (this.getTimer().isFinished()) {
            game.getEnemies().spawnX = this.getX();
            game.getEnemies().spawnY = this.getY();
            game.getEnemies().mkE(game.getPlayer());
        }
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
