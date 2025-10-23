package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.entities.npc.NpcManager;
import builder.entities.npc.Npc;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class NpcManagerTest {

    @Test
    public void testConstructor() {
        NpcManager manager = new NpcManager();
        Assert.assertNotNull("Manager should be created", manager);
        Assert.assertEquals("Initial NPC list should be empty", 0, manager.npcs.size());
    }

    @Test
    public void testAddNpc() {
        NpcManager manager = new NpcManager();
        TestNpc npc = new TestNpc(100, 200);
        
        manager.addNpc(npc);
        
        Assert.assertEquals("Should have 1 NPC", 1, manager.npcs.size());
        Assert.assertTrue("Should contain added NPC", manager.npcs.contains(npc));
    }

    @Test
    public void testAddMultipleNpcs() {
        NpcManager manager = new NpcManager();
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        
        Assert.assertEquals("Should have 2 NPCs", 2, manager.npcs.size());
    }

    @Test
    public void testCleanupRemovesMarkedNpcs() {
        NpcManager manager = new NpcManager();
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        TestNpc npc3 = new TestNpc(500, 600);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        manager.addNpc(npc3);
        
        // Mark npc2 for removal
        npc2.markForRemoval();
        
        manager.cleanup();
        
        Assert.assertEquals("Should have 2 NPCs after cleanup", 2, manager.npcs.size());
        Assert.assertTrue("Should still contain npc1", manager.npcs.contains(npc1));
        Assert.assertFalse("Should not contain npc2", manager.npcs.contains(npc2));
        Assert.assertTrue("Should still contain npc3", manager.npcs.contains(npc3));
    }

    @Test
    public void testCleanupIteratesBackwards() {
        NpcManager manager = new NpcManager();
        
        // Add multiple NPCs and mark them all for removal
        for (int i = 0; i < 5; i++) {
            TestNpc npc = new TestNpc(i * 100, i * 100);
            npc.markForRemoval();
            manager.addNpc(npc);
        }
        
        manager.cleanup();
        
        Assert.assertEquals("All marked NPCs should be removed", 0, manager.npcs.size());
    }

    @Test
    public void testCleanupWithNoMarkedNpcs() {
        NpcManager manager = new NpcManager();
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        
        manager.cleanup();
        
        Assert.assertEquals("Should still have 2 NPCs", 2, manager.npcs.size());
    }

    @Test
    public void testCleanupConditionalChecks() {
        NpcManager manager = new NpcManager();
        TestNpc unmarked = new TestNpc(100, 200);
        TestNpc marked = new TestNpc(300, 400);
        marked.markForRemoval();
        
        manager.addNpc(unmarked);
        manager.addNpc(marked);
        
        manager.cleanup();
        
        Assert.assertEquals("Only unmarked NPC should remain", 1, manager.npcs.size());
        Assert.assertEquals("Remaining NPC should be the unmarked one", unmarked, manager.npcs.get(0));
    }

    @Test
    public void testTickCallsCleanup() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestNpc markedNpc = new TestNpc(100, 200);
        markedNpc.markForRemoval();
        manager.addNpc(markedNpc);
        
        manager.tick(engine, game);
        
        Assert.assertEquals("Marked NPC should be cleaned up during tick", 0, manager.npcs.size());
    }

    @Test
    public void testTickCallsNpcTick() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestNpc npc = new TestNpc(100, 200);
        manager.addNpc(npc);
        
        Assert.assertFalse("Tick should not be called initially", npc.tickCalled);
        
        manager.tick(engine, game);
        
        Assert.assertTrue("Tick should be called on NPC", npc.tickCalled);
    }

    @Test
    public void testTickCallsAllNpcsTick() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        TestNpc npc3 = new TestNpc(500, 600);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        manager.addNpc(npc3);
        
        manager.tick(engine, game);
        
        Assert.assertTrue("Npc1 tick should be called", npc1.tickCalled);
        Assert.assertTrue("Npc2 tick should be called", npc2.tickCalled);
        Assert.assertTrue("Npc3 tick should be called", npc3.tickCalled);
    }

    @Test
    public void testInteractCallsInteractableInteract() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestInteractableNpc npc = new TestInteractableNpc(100, 200);
        manager.addNpc(npc);
        
        Assert.assertFalse("Interact should not be called initially", npc.interactCalled);
        
        manager.interact(engine, game);
        
        Assert.assertTrue("Interact should be called on interactable NPC", npc.interactCalled);
    }

    @Test
    public void testInteractOnlyCallsInteractableNpcs() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestNpc regularNpc = new TestNpc(100, 200);
        TestInteractableNpc interactableNpc = new TestInteractableNpc(300, 400);
        
        manager.addNpc(regularNpc);
        manager.addNpc(interactableNpc);
        
        manager.interact(engine, game);
        
        Assert.assertTrue("Interactable NPC should have interact called", interactableNpc.interactCalled);
    }

    @Test
    public void testGetInteractablesReturnsOnlyInteractables() {
        NpcManager manager = new NpcManager();
        
        TestNpc regularNpc = new TestNpc(100, 200);
        TestInteractableNpc interactableNpc = new TestInteractableNpc(300, 400);
        
        manager.addNpc(regularNpc);
        manager.addNpc(interactableNpc);
        
        // We need to use reflect to test private method, or test through interact
        manager.interact(new MockEngineState(), new TestGameState());
        
        Assert.assertTrue("Only interactable should have interact called", interactableNpc.interactCalled);
    }

    @Test
    public void testGetInteractablesConditionalCheck() {
        NpcManager manager = new NpcManager();
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestInteractableNpc interactable1 = new TestInteractableNpc(100, 200);
        TestInteractableNpc interactable2 = new TestInteractableNpc(300, 400);
        TestNpc nonInteractable = new TestNpc(500, 600);
        
        manager.addNpc(interactable1);
        manager.addNpc(nonInteractable);
        manager.addNpc(interactable2);
        
        manager.interact(engine, game);
        
        Assert.assertTrue("Interactable1 should be called", interactable1.interactCalled);
        Assert.assertTrue("Interactable2 should be called", interactable2.interactCalled);
    }

    @Test
    public void testRenderReturnsNonEmptyList() {
        NpcManager manager = new NpcManager();
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        
        java.util.List<engine.renderer.Renderable> renderables = manager.render();
        
        Assert.assertNotNull("Render should return non-null list", renderables);
        Assert.assertEquals("Should return list with 2 items", 2, renderables.size());
    }

    @Test
    public void testRenderReturnsEmptyListWhenNoNpcs() {
        NpcManager manager = new NpcManager();
        
        java.util.List<engine.renderer.Renderable> renderables = manager.render();
        
        Assert.assertNotNull("Render should return non-null list", renderables);
        Assert.assertEquals("Should return empty list", 0, renderables.size());
    }

    @Test
    public void testGetAllNpcsReturnsAllNpcs() {
        NpcManager manager = new NpcManager();
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(300, 400);
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        
        java.util.List<Npc> allNpcs = manager.getAllNpcs();
        
        Assert.assertNotNull("GetAllNpcs should return non-null list", allNpcs);
        Assert.assertEquals("Should return list with 2 NPCs", 2, allNpcs.size());
        Assert.assertTrue("Should contain npc1", allNpcs.contains(npc1));
        Assert.assertTrue("Should contain npc2", allNpcs.contains(npc2));
    }

    @Test
    public void testGetAllNpcsReturnsEmptyList() {
        NpcManager manager = new NpcManager();
        
        java.util.List<Npc> allNpcs = manager.getAllNpcs();
        
        Assert.assertNotNull("GetAllNpcs should return non-null list", allNpcs);
        Assert.assertEquals("Should return empty list", 0, allNpcs.size());
    }

    @Test
    public void testGetAllNpcsReturnsDefensiveCopy() {
        NpcManager manager = new NpcManager();
        TestNpc npc = new TestNpc(100, 200);
        manager.addNpc(npc);
        
        java.util.List<Npc> copy = manager.getAllNpcs();
        copy.clear();
        
        Assert.assertEquals("Original list should not be affected", 1, manager.npcs.size());
    }

    @Test
    public void testCleanupLoopBoundaryCondition() {
        NpcManager manager = new NpcManager();
        
        // Test with single NPC at boundary
        TestNpc npc = new TestNpc(100, 200);
        npc.markForRemoval();
        manager.addNpc(npc);
        
        manager.cleanup();
        
        Assert.assertEquals("Single marked NPC should be removed", 0, manager.npcs.size());
    }

    @Test
    public void testCleanupMultipleMarkedInRow() {
        NpcManager manager = new NpcManager();
        
        TestNpc npc1 = new TestNpc(100, 200);
        TestNpc npc2 = new TestNpc(200, 300);
        TestNpc npc3 = new TestNpc(300, 400);
        
        npc1.markForRemoval();
        npc2.markForRemoval();
        
        manager.addNpc(npc1);
        manager.addNpc(npc2);
        manager.addNpc(npc3);
        
        manager.cleanup();
        
        Assert.assertEquals("Two marked NPCs should be removed", 1, manager.npcs.size());
        Assert.assertEquals("Only npc3 should remain", npc3, manager.npcs.get(0));
    }

    // Test helper classes
    private static class TestNpc extends Npc {
        public boolean tickCalled = false;
        
        public TestNpc(int x, int y) {
            super(x, y);
        }
        
        @Override
        public void tick(engine.EngineState state, builder.GameState game) {
            tickCalled = true;
        }
    }

    private static class TestInteractableNpc extends Npc {
        public boolean interactCalled = false;
        
        public TestInteractableNpc(int x, int y) {
            super(x, y);
        }
        
        @Override
        public void tick(engine.EngineState state, builder.GameState game) {}
        
        @Override
        public void interact(engine.EngineState state, builder.GameState game) {
            interactCalled = true;
        }
    }

    private static class TestGameState implements builder.GameState {
        public TestNpcManager npcs = new TestNpcManager();
        public TestEnemyManager enemies = new TestEnemyManager();
        public TestInventory inventory = new TestInventory();
        public TestPlayer player = new TestPlayer();
        public TestWorld world = new TestWorld();

        @Override
        public builder.entities.npc.NpcManager getNpcs() {
            return npcs;
        }

        @Override
        public builder.entities.npc.enemies.EnemyManager getEnemies() {
            return enemies;
        }

        @Override
        public builder.inventory.Inventory getInventory() {
            return inventory;
        }

        @Override
        public builder.player.Player getPlayer() {
            return player;
        }

        @Override
        public builder.world.World getWorld() {
            return world;
        }
    }

    private static class TestNpcManager extends builder.entities.npc.NpcManager {
        @Override
        public void tick(engine.EngineState state, builder.GameState game) {}
    }

    private static class TestEnemyManager extends builder.entities.npc.enemies.EnemyManager {
        public TestEnemyManager() {
            super(new engine.renderer.TileGrid(25, 2000));
        }
        
        @Override
        public void tick(engine.EngineState state, builder.GameState game) {}
    }

    private static class TestInventory implements builder.inventory.Inventory {
        private int food = 10;
        private int coins = 10;

        @Override
        public int getFood() {
            return food;
        }

        @Override
        public void addFood(int amount) {
            food += amount;
        }

        @Override
        public int getCoins() {
            return coins;
        }

        @Override
        public void addCoins(int amount) {
            coins += amount;
        }

        @Override
        public int getActiveSlot() {
            return 0;
        }

        @Override
        public void setActiveSlot(int slot) {}
        
        @Override
        public builder.inventory.items.Item getHolding() {
            return null;
        }
        
        @Override
        public builder.inventory.items.Item getItem(int slot) {
            return null;
        }
        
        @Override
        public void setItem(int slot, builder.inventory.items.Item item) {}
        
        @Override
        public int getCapacity() {
            return 10;
        }
    }

    private static class TestPlayer implements builder.player.Player {
        private int x = 100;
        private int y = 200;

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
        
        @Override
        public void setX(int x) {
            this.x = x;
        }
        
        @Override
        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String getID() {
            return "test-player";
        }

        @Override
        public int getDamage() {
            return 0;
        }
    }

    private static class TestWorld implements builder.world.World {
        @Override
        public java.util.List<builder.entities.tiles.Tile> tilesAtPosition(
                int x, int y, engine.renderer.Dimensions dimensions) {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.List<builder.entities.tiles.Tile> allTiles() {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.List<builder.entities.tiles.Tile> tileSelector(
                java.util.function.Predicate<builder.entities.tiles.Tile> filter) {
            return new java.util.ArrayList<>();
        }

        @Override
        public void place(builder.entities.tiles.Tile tile) {}
    }
}

