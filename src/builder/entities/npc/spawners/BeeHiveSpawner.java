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

    RepeatingTimer timer;
    private int xPos = 0;
    private int yPos = 0;

    /**
     * Constructs a BeeHiveSpawner at the specified position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param duration the timer duration
     */
    public BeeHiveSpawner(int x, int y, int duration) {
        this.xPos = x;
        this.yPos = y;
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
