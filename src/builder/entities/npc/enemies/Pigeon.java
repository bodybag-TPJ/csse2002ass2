package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

/**
 * A pigeon enemy that attacks cabbages and can return to spawn point.
 */
public class Pigeon extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.pigeon;
    private FixedTimer lifespan = new FixedTimer(3000);
    private HasPosition trackedTarget;
    private Boolean attacking = true;
    private int spawnX = 0;
    private int spawnY = 0;

    /**
     * Constructs a Pigeon at the specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Pigeon(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.setSpeed(4);
        this.setSprite(art.getSprite("down"));
    }

    /**
     * Constructs a Pigeon with a tracked target.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param trackedTarget the target to track
     */
    public Pigeon(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setSpeed(4);
        this.setSprite(art.getSprite("down"));
    }

    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    /**
     * Gets the attacking state.
     *
     * @return true if attacking
     */
    public Boolean getAttacking() {
        return attacking;
    }

    /**
     * Sets the attacking state.
     *
     * @param attacking the new attacking state
     */
    public void setAttacking(Boolean attacking) {
        this.attacking = attacking;
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        if (!this.attacking) {
            double deltaX = (this.spawnX - this.getX());
            double deltaY = (this.spawnY - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));

            if (this.distanceFrom(this.spawnX, this.spawnY)
                    < engine.getDimensions().tileSize()) { // get close to spawn
                this.markForRemoval();
            }
            if (this.spawnY < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }
        
        if (this.trackedTarget == null && this.attacking) { 
            double deltaX;
            double deltaY;
            if (this.getX() < 400) {
                deltaX = (450 - this.getX());
                deltaY = (900 - this.getY());
            } else {
                deltaX = (850 - this.getX());
                deltaY = (850 - this.getY());
            }
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (deltaY > 0) {
                this.setSprite(art.getSprite("down"));
            } else {
                this.setSprite(art.getSprite("up"));
            }
        }
        
        if (this.trackedTarget != null && this.attacking) {
            double deltaX = (this.trackedTarget.getX() - this.getX());
            double deltaY = (this.trackedTarget.getY() - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        }
        
        this.move();
        this.lifespan.tick();
        if (this.lifespan.isFinished()) {
            this.markForRemoval();
        }
        
        if (!attacking) {
            if (this.distanceFrom(spawnX, spawnY) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
            if (this.spawnY < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }

        List<Tile> tiles =
                game.getWorld()
                        .tileSelector(
                                tile -> {
                                    for (Entity entity : tile.getStackedEntities()) {
                                        if (entity instanceof Cabbage) {
                                            return true;
                                        } else {
                                            // do nothing
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
                    distance = this.distanceFrom(tile);
                } else {
                    // do nothing
                }
            }
            this.trackedTarget = closest;

            int distanceToTarget = this.distanceFrom(this.trackedTarget);
            int tileSize = engine.getDimensions().tileSize();
            if (this.attacking && distanceToTarget < tileSize) {
                for (Entity entity : closest.getStackedEntities()) {
                    if (entity instanceof Cabbage cabbage) {
                        cabbage.markForRemoval();
                        this.attacking = false;
                        break;
                    } else {
                        // do nothing
                    }
                }
            }
        } else { // no cabbages to get
            this.attacking = false;
        }
    }
}