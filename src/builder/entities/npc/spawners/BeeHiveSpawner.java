package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.BeeHive;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * A spawner that creates BeeHive entities based on user input.
 */
public class BeeHiveSpawner implements Spawner {

    private RepeatingTimer timer;
    private int x0 = 0;
    private int y0 = 0;

    /**
     * Constructs a BeeHiveSpawner at the specified position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the timer duration
     */
    public BeeHiveSpawner(int x, int y, int duration) {
        this.x0 = x;
        this.y0 = y;
        this.timer = new RepeatingTimer(300);
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        timer.tick();
        final boolean canAfford =
                game.getInventory().getFood() >= 3 && game.getInventory().getCoins() >= 3;

        if (canAfford && state.getKeys().isDown('h')) {
            game.getInventory().addFood(-3);
            game.getInventory().addCoins(-3);
            game.getNpcs().npcs.add(new BeeHive(game.getPlayer().getX(), game.getPlayer().getY()));
        }
        // look at use code example to spawn based on user input and only on grass tiles
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
