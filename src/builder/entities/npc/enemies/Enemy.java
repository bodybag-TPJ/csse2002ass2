package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;

/**
 * Base class for all enemy entities in the game.
 */
public class Enemy extends Npc {
    /**
     * Creates a new Enemy at the specified coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public void interact(EngineState state, GameState game) {}
}
