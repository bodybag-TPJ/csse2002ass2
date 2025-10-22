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
    public final ArrayList<Enemy> Birds = new ArrayList<>();
    public int spawnX;
    public int spawnY;

    /**
     * Creates a new EnemyManager for the given game dimensions.
     * 
     * @param dimensions The game world dimensions
     */
    public EnemyManager(Dimensions dimensions) {
        // Currently dimensions are not used but may be needed for future features
    }

    /**
     * Removes all enemies marked for removal from the active enemy list.
     */
    public void cleanup() {
        for (int i = this.Birds.size() - 1; i >= 0; i -= 1) {
            if (this.Birds.get(i).isMarkedForRemoval()) {
                this.Birds.remove(i);
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
        final Magpie magpie = new Magpie(this.spawnX, this.spawnY, player);
        this.Birds.add(magpie);
        return magpie;
    }

    /**
     * Creates and spawns a new Pigeon at the current spawn location.
     * 
     * @param target The target position for the pigeon
     * @return The created pigeon
     */
    public Pigeon mkP(HasPosition target) {
        final Pigeon pigeon = new Pigeon(this.spawnX, this.spawnY, target);
        this.Birds.add(pigeon);
        return pigeon;
    }

    /**
     * Creates and spawns a new Eagle at the current spawn location.
     * 
     * @param player The player target for the eagle
     * @return The created eagle
     */
    public Eagle mkE(Player player) {
        final Eagle eagle = new Eagle(this.spawnX, this.spawnY, player);
        this.Birds.add(eagle);
        return eagle;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Spawner spawner : this.spawners) {
            spawner.tick(state, game);
        }
        for (Enemy bird : Birds) {
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
        for (Enemy bird : Birds) {
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
    public ArrayList<Enemy> getALl() {
        return this.Birds;
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
        return new ArrayList<>(this.Birds);
    }
}