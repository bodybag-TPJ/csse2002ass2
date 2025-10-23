package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all non-player characters (NPCs) in the game.
 * Handles lifecycle, interactions, and rendering of NPCs such as BeeHives and Scarecrows.
 */
public class NpcManager implements Interactable, Tickable, RenderableGroup {
    
    /** List of all managed NPCs. */
    private final ArrayList<Npc> npcs = new ArrayList<>();

    /**
     * Creates a new NpcManager with an empty list of NPCs.
     */
    public NpcManager() {
        // No initialization needed
    }

    /**
     * Gets the internal list of NPCs for direct access.
     * 
     * @return The list of NPCs
     */
    public ArrayList<Npc> getNpcs() {
        return this.npcs;
    }

    /**
     * Removes all NPCs marked for removal from the active NPC list.
     */
    public void cleanup() {
        for (int i = this.getNpcs().size() - 1; i >= 0; i -= 1) {
            if (this.getNpcs().get(i).isMarkedForRemoval()) {
                this.getNpcs().remove(i);
            }
        }
    }

    /**
     * Adds an NPC to this manager for tracking and management.
     * 
     * @param npc The NPC to add to the manager
     */
    public void addNpc(Npc npc) {
        this.getNpcs().add(npc);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Npc npc : npcs) {
            npc.tick(state, game);
        }
    }

    @Override
    public void interact(EngineState state, GameState game) {
        for (Interactable interactable : this.getInteractables()) {
            interactable.interact(state, game);
        }
    }

    /**
     * Gets all NPCs that implement the Interactable interface.
     * 
     * @return A list of interactable NPCs
     */
    private List<Interactable> getInteractables() {
        final List<Interactable> interactables = new ArrayList<>();
        for (Npc npc : npcs) {
            if (npc instanceof Interactable interactable) {
                interactables.add(interactable);
            }
        }
        return interactables;
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.npcs);
    }

    /**
     * Gets all NPCs currently managed by this manager.
     * 
     * @return A defensive copy of all active NPCs
     */
    public List<Npc> getAllNpcs() {
        return new ArrayList<>(this.npcs);
    }
}