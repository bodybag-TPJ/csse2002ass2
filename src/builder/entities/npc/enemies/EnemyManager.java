package builder.entities.npc.enemies;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all enemy entities including birds and their spawners.
 * Handles spawning, updating, and cleanup of enemy entities.
 */
public class EnemyManager implements Tickable, Interactable, RenderableGroup {

    private final ArrayList<Spawner> spawners = new ArrayList<>();
    private final ArrayList<Enemy> birds = new ArrayList<>();
    private int spawnX;
    private int spawnY;

    /**
     * Creates a new EnemyManager for the given game dimensions.
     * 
     * @param dimensions The game world dimensions
     */
    public EnemyManager(Dimensions dimensions) {
        // Currently dimensions are not used but may be needed for future features
    }

    /**
     * Gets the list of all enemies (birds).
     * 
     * @return The list of enemies
     */
    public ArrayList<Enemy> getBirds() {
        return this.birds;
    }

    /**
     * Gets the current spawn X coordinate.
     * 
     * @return The spawn X coordinate
     */
    public int getSpawnX() {
        return this.spawnX;
    }

    /**
     * Sets the spawn X coordinate.
     * 
     * @param spawnX The new spawn X coordinate
     */
    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    /**
     * Gets the current spawn Y coordinate.
     * 
     * @return The spawn Y coordinate
     */
    public int getSpawnY() {
        return this.spawnY;
    }

    /**
     * Sets the spawn Y coordinate.
     * 
     * @param spawnY The new spawn Y coordinate
     */
    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    /**
     * Removes all enemies marked for removal from the active enemy list.
     */
    public void cleanup() {
        for (int i = this.birds.size() - 1; i >= 0; i -= 1) {
            if (this.birds.get(i).isMarkedForRemoval()) {
                this.birds.remove(i);
            }
        }
    }

    /**
     * Adds a spawner to this enemy manager.
     * 
     * @param spawner The spawner to add
     */
    public void add(Spawner spawner) {
        this.spawners.add(spawner);
    }

    /**
     * Creates and spawns a new Magpie at the current spawn location.
     * 
     * @param player The player target for the magpie
     * @return The created magpie
     */
    public Magpie mkM(Player player) {
        final Magpie magpie = new Magpie(this.getSpawnX(), this.getSpawnY(), player);
        this.birds.add(magpie);
        return magpie;
    }

    /**
     * Creates and spawns a new Pigeon at the current spawn location.
     * 
     * @param target The target position for the pigeon
     * @return The created pigeon
     */
    public Pigeon mkP(HasPosition target) {
        final Pigeon pigeon = new Pigeon(this.getSpawnX(), this.getSpawnY(), target);
        this.birds.add(pigeon);
        return pigeon;
    }

    /**
     * Creates and spawns a new Eagle at the current spawn location.
     * 
     * @param player The player target for the eagle
     * @return The created eagle
     */
    public Eagle mkE(Player player) {
        final Eagle eagle = new Eagle(this.getSpawnX(), this.getSpawnY(), player);
        this.birds.add(eagle);
        return eagle;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Spawner spawner : this.spawners) {
            spawner.tick(state, game);
        }
        for (Enemy bird : birds) {
            bird.tick(state, game);
        }
    }

    /**
     * Gets all magpies currently managed by this enemy manager.
     * 
     * @return A list of all active magpies
     */
    public ArrayList<Magpie> getMagpies() {
        final ArrayList<Magpie> magpies = new ArrayList<>();
        for (Enemy bird : birds) {
            if (bird instanceof Magpie magpie) {
                magpies.add(magpie);
            }
        }
        return magpies;
    }

    /**
     * Gets all enemies currently managed by this enemy manager.
     * 
     * @return A reference to all active enemies
     */
    public ArrayList<Enemy> getAll() {
        return this.birds;
    }

    /**
     * Handles interaction events. Currently not implemented.
     * 
     * @param state The state of the engine, including the mouse, keyboard information and
     *     dimension. Useful for processing keyboard presses or mouse movement.
     * @param game The state of the game, including the player and world. Can be used to query or
     *     update the game state.
     */
    @Override
    public void interact(EngineState state, GameState game) {
        // Implementation not currently needed
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.birds);
    }
}