package builder.entities.npc;

/**
 * Interface for entities that can have a direction and can move.
 */
public interface Directable {

    /**
     * Gets the current direction of the entity.
     *
     * @return The direction in degrees
     */
    public int getDirection();

    /**
     * Sets the direction of the entity.
     *
     * @param direction The direction in degrees
     */
    public void setDirection(int direction);

    /**
     * Moves the entity in its current direction.
     */
    public void move();
}
