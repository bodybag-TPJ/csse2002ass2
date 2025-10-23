package builder.world;

import engine.game.ImmutablePosition;

/**
 * Details for the player in the world.
 */
public interface PlayerDetails extends ImmutablePosition {
    /**
     * Gets the starting food amount.
     *
     * @return starting food
     */
    int getStartingFood();

    /**
     * Gets the starting coins amount.
     *
     * @return starting coins
     */
    int getStartingCoins();
}
