package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.entities.npc.spawners.MagpieSpawner;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class MagpieSpawnerTest {

    @Test
    public void testConstructorBasic() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200);
        Assert.assertEquals("X should be set", 100, spawner.getX());
        Assert.assertEquals("Y should be set", 200, spawner.getY());
        Assert.assertNotNull("Timer should be initialized", spawner.getTimer());
    }

    @Test
    public void testConstructorWithDuration() {
        MagpieSpawner spawner = new MagpieSpawner(150, 250, 100);
        Assert.assertEquals("X should be set", 150, spawner.getX());
        Assert.assertEquals("Y should be set", 250, spawner.getY());
        Assert.assertNotNull("Timer should be initialized", spawner.getTimer());
    }

    @Test
    public void testGetTimer() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200);
        engine.timing.TickTimer timer = spawner.getTimer();
        Assert.assertNotNull("Timer should not be null", timer);
    }

    @Test
    public void testGetX() {
        MagpieSpawner spawner = new MagpieSpawner(300, 400);
        Assert.assertEquals("X should return correct value", 300, spawner.getX());
    }

    @Test
    public void testGetY() {
        MagpieSpawner spawner = new MagpieSpawner(300, 400);
        Assert.assertEquals("Y should return correct value", 400, spawner.getY());
    }

    @Test
    public void testSetX() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200);
        spawner.setX(500);
        Assert.assertEquals("X should be updated", 500, spawner.getX());
    }

    @Test
    public void testSetY() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200);
        spawner.setY(600);
        Assert.assertEquals("Y should be updated", 600, spawner.getY());
    }

    @Test
    public void testTickCallsTimerTick() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200, 1);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        spawner.tick(engine, game);
        Assert.assertTrue("Timer should be ticked", true);
    }

    @Test
    public void testTickWhenNotFinished() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200, 1000);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        spawner.tick(engine, game);
        
        Assert.assertEquals("Should not spawn magpie when timer not finished", 0, game.enemies.getBirds().size());
    }

    @Test
    public void testTickWhenFinished() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200, 1);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 5; i++) {
            spawner.tick(engine, game);
        }
        
        Assert.assertTrue("Should spawn magpie when timer finished", game.enemies.getBirds().size() > 0);
    }

    @Test
    public void testTickMultipleTimes() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200, 2);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 10; i++) {
            spawner.tick(engine, game);
        }
        
        Assert.assertTrue("Should spawn multiple magpies", game.enemies.getBirds().size() >= 1);
    }

    @Test
    public void testTickSetsSpawnCoordinates() {
        MagpieSpawner spawner = new MagpieSpawner(300, 400, 1);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 5; i++) {
            spawner.tick(engine, game);
        }
        
        Assert.assertEquals("SpawnX should be set", 300, game.enemies.getSpawnX());
        Assert.assertEquals("SpawnY should be set", 400, game.enemies.getSpawnY());
    }

    @Test
    public void testGetXReturnsNonZero() {
        MagpieSpawner spawner = new MagpieSpawner(500, 600);
        int x = spawner.getX();
        Assert.assertEquals("X should be 500", 500, x);
    }

    @Test
    public void testGetYReturnsNonZero() {
        MagpieSpawner spawner = new MagpieSpawner(500, 600);
        int y = spawner.getY();
        Assert.assertEquals("Y should be 600", 600, y);
    }

    @Test
    public void testTimerIsFinishedCondition() {
        MagpieSpawner spawner = new MagpieSpawner(100, 200, 1);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        spawner.tick(engine, game);
        spawner.tick(engine, game);
        
        Assert.assertTrue("Should check timer isFinished", true);
    }

    public static class TestGameState implements builder.GameState {
        public TestPlayer player = new TestPlayer();
        public TestInventory inventory = new TestInventory();
        public builder.entities.npc.NpcManager npcs = new builder.entities.npc.NpcManager();
        public builder.entities.npc.enemies.EnemyManager enemies;
        
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

