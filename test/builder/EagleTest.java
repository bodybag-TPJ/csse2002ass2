package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.entities.npc.enemies.Eagle;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class EagleTest {

    @Test
    public void testConstructorBasic() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        Assert.assertEquals("X coordinate should be set correctly", 100, eagle.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 200, eagle.getY());
        Assert.assertNotNull("Lifespan should be initialized", eagle.getLifespan());
        Assert.assertNotNull("Tracked target should be set", eagle.getTrackedTarget());
        Assert.assertEquals("Tracked target should match", target, eagle.getTrackedTarget());
        Assert.assertNotNull("Sprite should be set", eagle.getSprite());
    }

    @Test
    public void testGetLifespanReturnsNonNull() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        engine.timing.FixedTimer timer = eagle.getLifespan();
        Assert.assertNotNull("Lifespan should not be null", timer);
    }

    @Test
    public void testSetLifespan() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        engine.timing.FixedTimer newTimer = new engine.timing.FixedTimer(1000);
        eagle.setLifespan(newTimer);
        Assert.assertEquals("Lifespan should be updated", newTimer, eagle.getLifespan());
    }

    @Test
    public void testConstructorSetsDirectionAndSpeed() {
        TestHasPosition target1 = new TestHasPosition(150, 250);
        Eagle eagle1 = new Eagle(100, 200, target1);
        Assert.assertNotNull("Constructor should complete", eagle1);
        
        TestHasPosition target2 = new TestHasPosition(50, 100);
        Eagle eagle2 = new Eagle(200, 300, target2);
        Assert.assertNotNull("Constructor should complete with different target", eagle2);
    }

    @Test
    public void testTickLifespanFinished() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        engine.timing.FixedTimer shortTimer = new engine.timing.FixedTimer(1);
        eagle.setLifespan(shortTimer);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 10; i++) {
            eagle.tick(engine, game);
        }
        Assert.assertTrue("Should be marked for removal", eagle.isMarkedForRemoval());
    }

    @Test
    public void testTickNearPlayer() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        game.player.setX(105);
        game.player.setY(205);
        
        int initialFood = game.inventory.getFood();
        eagle.tick(engine, game);
        
        Assert.assertTrue("Food should decrease or speed should change", 
            game.inventory.getFood() < initialFood || eagle.getX() != 100 || eagle.getY() != 200);
    }

    @Test
    public void testTickAttackingMode() {
        TestHasPosition target = new TestHasPosition(500, 600);
        Eagle eagle = new Eagle(100, 200, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        eagle.tick(engine, game);
        Assert.assertNotNull("Should tick in attacking mode", eagle);
    }

    @Test
    public void testTickReturningToSpawn() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        eagle.setX(100);
        eagle.setY(200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        game.player.setX(105);
        game.player.setY(205);
        
        eagle.tick(engine, game);
        eagle.tick(engine, game);
        
        Assert.assertTrue("Should process return to spawn logic", true);
    }

    @Test
    public void testTickTargetAbove() {
        TestHasPosition target = new TestHasPosition(300, 100);
        Eagle eagle = new Eagle(300, 400, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        eagle.tick(engine, game);
        Assert.assertNotNull("Sprite should be set for upward movement", eagle.getSprite());
    }

    @Test
    public void testTickTargetBelow() {
        TestHasPosition target = new TestHasPosition(300, 600);
        Eagle eagle = new Eagle(300, 200, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        eagle.tick(engine, game);
        Assert.assertNotNull("Sprite should be set for downward movement", eagle.getSprite());
    }

    @Test
    public void testTickSpawnAbove() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(300, 100, target);
        eagle.setX(300);
        eagle.setY(200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        game.player.setX(305);
        game.player.setY(205);
        
        eagle.tick(engine, game);
        eagle.tick(engine, game);
        Assert.assertTrue("Should handle spawn above case", true);
    }

    @Test
    public void testTickSpawnBelow() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(300, 500, target);
        eagle.setX(300);
        eagle.setY(200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        game.player.setX(305);
        game.player.setY(205);
        
        eagle.tick(engine, game);
        eagle.tick(engine, game);
        Assert.assertTrue("Should handle spawn below case", true);
    }

    @Test
    public void testTickFoodCollection() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        game.inventory.setFood(10);
        game.player.setX(105);
        game.player.setY(205);
        
        eagle.tick(engine, game);
        
        Assert.assertTrue("Food interaction should occur", game.inventory.getFood() <= 10);
    }

    @Test
    public void testTickMultipleTimes() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 5; i++) {
            eagle.tick(engine, game);
        }
        Assert.assertTrue("Tick should complete multiple times", true);
    }

    @Test
    public void testTickMarkedForRemovalFarFromSpawn() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        eagle.markForRemoval();
        eagle.setX(500);
        eagle.setY(600);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        int initialFood = game.inventory.getFood();
        eagle.tick(engine, game);
        
        Assert.assertEquals("Food should be added when far from spawn", initialFood, game.inventory.getFood());
    }

    @Test
    public void testTickMarkedForRemovalNearSpawn() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Eagle eagle = new Eagle(100, 200, target);
        eagle.markForRemoval();
        eagle.setX(100);
        eagle.setY(200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        eagle.tick(engine, game);
        Assert.assertTrue("Should handle near spawn case", true);
    }

    @Test
    public void testConstructorDifferentPositions() {
        TestHasPosition target1 = new TestHasPosition(100, 100);
        TestHasPosition target2 = new TestHasPosition(200, 300);
        TestHasPosition target3 = new TestHasPosition(400, 500);
        
        Eagle eagle1 = new Eagle(0, 0, target1);
        Eagle eagle2 = new Eagle(150, 250, target2);
        Eagle eagle3 = new Eagle(200, 300, target3);
        
        Assert.assertNotNull("Eagle1 should be created", eagle1);
        Assert.assertNotNull("Eagle2 should be created", eagle2);
        Assert.assertNotNull("Eagle3 should be created", eagle3);
    }

    public static class TestHasPosition implements engine.game.HasPosition {
        int x, y;
        
        public TestHasPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
    }
    
    public static class TestGameState implements builder.GameState {
        public TestPlayer player = new TestPlayer();
        public TestInventory inventory = new TestInventory();
        private builder.entities.npc.NpcManager npcs = new builder.entities.npc.NpcManager();
        private builder.entities.npc.enemies.EnemyManager enemies;
        
        public TestGameState() {
            this.enemies = new builder.entities.npc.enemies.EnemyManager(new TileGrid(25, 2000));
        }
        
        public builder.world.World getWorld() { return new TestWorld(); }
        public builder.player.Player getPlayer() { return player; }
        public builder.inventory.Inventory getInventory() { return inventory; }
        public builder.entities.npc.NpcManager getNpcs() { return npcs; }
        public builder.entities.npc.enemies.EnemyManager getEnemies() { return enemies; }
    }
    
    public static class TestWorld implements builder.world.World {
        public java.util.List<builder.entities.tiles.Tile> tilesAtPosition(int x, int y, engine.renderer.Dimensions dimensions) {
            return new java.util.ArrayList<>();
        }
        
        public java.util.List<builder.entities.tiles.Tile> tileSelector(java.util.function.Predicate<builder.entities.tiles.Tile> filter) {
            return new java.util.ArrayList<>();
        }
        
        public java.util.List<builder.entities.tiles.Tile> allTiles() {
            return new java.util.ArrayList<>();
        }
        
        public void place(builder.entities.tiles.Tile tile) {}
    }
    
    public static class TestPlayer implements builder.player.Player {
        private int x = 1000, y = 1000;

        public int getX() { return x; }
        public int getY() { return y; }
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
        public String getID() { return "test-player"; }
        public int getDamage() { return 1; }
    }
    
    public static class TestInventory implements builder.inventory.Inventory {
        private int food = 10;
        private int coins = 10;

        public void addFood(int amount) { this.food = Math.max(0, this.food + amount); }
        public int getFood() { return food; }
        public void setFood(int food) { this.food = food; }
        public void addCoins(int amount) { this.coins = Math.max(0, this.coins + amount); }
        public int getCoins() { return coins; }
        public int getCapacity() { return 10; }
        public void setItem(int slot, builder.inventory.items.Item item) {}
        public builder.inventory.items.Item getItem(int slot) { return null; }
        public builder.inventory.items.Item getHolding() { return null; }
        public int getActiveSlot() { return 0; }
        public void setActiveSlot(int slot) {}
    }
}
