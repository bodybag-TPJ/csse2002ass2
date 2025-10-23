package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

/**
 * Base class for all non-player characters in the game.
 */
public class Npc extends Entity implements Interactable, Tickable, Directable {

    private int direction = 0;
    private double speed = 1;

    /**
     * Creates a new Npc at the specified coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Npc(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the current speed of this NPC.
     *
     * @return the speed value
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of this NPC.
     *
     * @param speed the new speed value
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the current direction of this NPC.
     *
     * @return the direction in degrees
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Sets the direction of this NPC.
     *
     * @param direction the new direction in degrees
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Adjusts the X and Y coordinates of this NPC based on direction and speed.
     */
    public void move() {
        final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
        final int deltaY = (int) Math.round(Math.sin(Math.toRadians(this.direction)) * this.speed);
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    @Override
    public void tick(EngineState state) {
        this.move();
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.move();
    }

    @Override
    public void interact(EngineState state, GameState game) {}

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

    /**
     * Return how far away this npc is from the given position
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(int x, int y) {
        int deltaX = x - this.getX();
        int deltaY = y - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
