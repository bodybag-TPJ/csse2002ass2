package builder.world;

import engine.game.HasPosition;

/**
 * Details for a spawner entity in the world.
 */
public interface SpawnerDetails extends HasPosition {
    /**
     * Gets the x coordinate.
     *
     * @return x coordinate
     */
    public int getX();

    /**
     * Gets the y coordinate.
     *
     * @return y coordinate
     */
    public int getY();

    /**
     * Sets the x coordinate.
     *
     * @param x the x coordinate
     */
    public void setX(int x);

    /**
     * Sets the y coordinate.
     *
     * @param y the y coordinate
     */
    public void setY(int y);

    /**
     * Gets the duration.
     *
     * @return the duration value
     */
    public int getDuration();
}
