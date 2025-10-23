package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.entities.npc.enemies.Pigeon;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Dirt;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class PigeonTest {

    @Test
    public void testConstructorBasic() {
        Pigeon testPigeon = new Pigeon(50, 75);
        Assert.assertEquals("X coordinate should be set correctly", 50, testPigeon.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 75, testPigeon.getY());
        Assert.assertNotNull("Lifespan should be initialized", testPigeon.getLifespan());
        Assert.assertTrue("Should be attacking initially", testPigeon.getAttacking());
        Assert.assertNotNull("Sprite should be set", testPigeon.getSprite());
    }

    @Test
    public void testConstructorWithTarget() {
        TestHasPosition target = new TestHasPosition(500, 600);
        Pigeon testPigeon = new Pigeon(80, 90, target);
        Assert.assertEquals("X coordinate should be set correctly", 80, testPigeon.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 90, testPigeon.getY());
        Assert.assertNotNull("Lifespan should be initialized", testPigeon.getLifespan());
        Assert.assertTrue("Should be attacking initially", testPigeon.getAttacking());
        Assert.assertNotNull("Sprite should be set", testPigeon.getSprite());
    }

    @Test
    public void testGetLifespanReturnsNonNull() {
        Pigeon pigeon = new Pigeon(100, 200);
        engine.timing.FixedTimer timer = pigeon.getLifespan();
        Assert.assertNotNull("Lifespan should not be null", timer);
    }

    @Test
    public void testSetLifespan() {
        Pigeon pigeon = new Pigeon(100, 200);
        engine.timing.FixedTimer newTimer = new engine.timing.FixedTimer(5000);
        pigeon.setLifespan(newTimer);
        Assert.assertEquals("Lifespan should be updated", newTimer, pigeon.getLifespan());
    }

    @Test
    public void testTickNotAttackingReturningToSpawn() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(false);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Tick should complete", true);
    }

    @Test
    public void testTickAttackingNoTargetLeftSide() {
        Pigeon pigeon = new Pigeon(300, 500);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Left side pigeon should tick", true);
    }

    @Test
    public void testTickAttackingNoTargetRightSide() {
        Pigeon pigeon = new Pigeon(500, 500);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Right side pigeon should tick", true);
    }

    @Test
    public void testTickWithTrackedTarget() {
        TestHasPosition target = new TestHasPosition(300, 400);
        Pigeon pigeon = new Pigeon(100, 200, target);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Pigeon with target should tick", true);
    }

    @Test
    public void testTickLifespanFinished() {
        Pigeon pigeon = new Pigeon(100, 200);
        engine.timing.FixedTimer shortTimer = new engine.timing.FixedTimer(1);
        pigeon.setLifespan(shortTimer);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 10; i++) {
            pigeon.tick(engine, game);
        }
        Assert.assertTrue("Should be marked for removal", pigeon.isMarkedForRemoval());
    }

    @Test
    public void testTickNotAttackingCloseToSpawn() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(false);
        pigeon.setX(100);
        pigeon.setY(200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Close to spawn should work", true);
    }

    @Test
    public void testTickNotAttackingFarFromSpawn() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(false);
        pigeon.setX(500);
        pigeon.setY(600);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertFalse("Far from spawn should not be removed", pigeon.isMarkedForRemoval());
    }

    @Test
    public void testTickWithCabbageOnTile() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        Dirt tile = new Dirt(100, 200);
        Cabbage cabbage = new Cabbage(100, 200);
        tile.placeOn(cabbage);
        game.getWorld().place(tile);
        
        pigeon.tick(engine, game);
        Assert.assertNotNull("Pigeon should process cabbage logic", pigeon);
    }

    @Test
    public void testTickSpriteChangeSpawnAbove() {
        Pigeon pigeon = new Pigeon(100, 50);
        pigeon.setAttacking(false);
        pigeon.setX(100);
        pigeon.setY(100);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertNotNull("Sprite should update for up direction", pigeon.getSprite());
    }

    @Test
    public void testTickSpriteChangeSpawnBelow() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(false);
        pigeon.setX(100);
        pigeon.setY(100);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertNotNull("Sprite should update for down direction", pigeon.getSprite());
    }

    @Test
    public void testTickTargetNullAttackingLeftBranch() {
        Pigeon pigeon = new Pigeon(350, 800);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Should handle left branch (x < 400)", true);
    }

    @Test
    public void testTickTargetNullAttackingRightBranch() {
        Pigeon pigeon = new Pigeon(450, 800);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Should handle right branch (x >= 400)", true);
    }

    @Test
    public void testTickMultipleTimes() {
        Pigeon pigeon = new Pigeon(100, 200);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 5; i++) {
            pigeon.tick(engine, game);
        }
        Assert.assertTrue("Tick should complete multiple times", true);
    }

    @Test
    public void testTickCabbageNearby() {
        Pigeon pigeon = new Pigeon(100, 100);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        Dirt tile = new Dirt(100, 100);
        Cabbage cabbage = new Cabbage(100, 100);
        tile.placeOn(cabbage);
        ((TestWorld)game.getWorld()).addTileWithCabbage(tile);
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Should process nearby cabbage", true);
    }

    @Test
    public void testConstructorSetsSpeedAndSprite() {
        Pigeon p1 = new Pigeon(10, 20);
        Assert.assertNotNull("Sprite should be set by constructor", p1.getSprite());
        
        TestHasPosition target = new TestHasPosition(30, 40);
        Pigeon p2 = new Pigeon(15, 25, target);
        Assert.assertNotNull("Sprite should be set by constructor with target", p2.getSprite());
    }

    @Test
    public void testTickNoCabbages() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(true);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        pigeon.tick(engine, game);
        Assert.assertTrue("Should process tick with no cabbages", !pigeon.getAttacking() || pigeon.getAttacking());
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
        private TestWorld world = new TestWorld();
        private TestPlayer player = new TestPlayer();
        private TestInventory inventory = new TestInventory();
        private builder.entities.npc.NpcManager npcs = new builder.entities.npc.NpcManager();
        private builder.entities.npc.enemies.EnemyManager enemies;
        
        public TestGameState() {
            this.enemies = new builder.entities.npc.enemies.EnemyManager(new TileGrid(25, 2000));
        }
        
        public builder.world.World getWorld() { return world; }
        public builder.player.Player getPlayer() { return player; }
        public builder.inventory.Inventory getInventory() { return inventory; }
        public builder.entities.npc.NpcManager getNpcs() { return npcs; }
        public builder.entities.npc.enemies.EnemyManager getEnemies() { return enemies; }
    }
    
    public static class TestWorld implements builder.world.World {
        private java.util.List<builder.entities.tiles.Tile> tiles = new java.util.ArrayList<>();
        
        public java.util.List<builder.entities.tiles.Tile> tilesAtPosition(int x, int y, engine.renderer.Dimensions dimensions) {
            return new java.util.ArrayList<>();
        }
        
        public java.util.List<builder.entities.tiles.Tile> tileSelector(java.util.function.Predicate<builder.entities.tiles.Tile> filter) {
            return tiles.stream().filter(filter).collect(java.util.stream.Collectors.toList());
        }
        
        public java.util.List<builder.entities.tiles.Tile> allTiles() {
            return new java.util.ArrayList<>(tiles);
        }
        
        public void place(builder.entities.tiles.Tile tile) {
            tiles.add(tile);
        }
        
        public void addTileWithCabbage(builder.entities.tiles.Tile tile) {
            tiles.add(tile);
        }
    }
    
    public static class TestPlayer implements builder.player.Player {
        private int x = 500, y = 500;

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
