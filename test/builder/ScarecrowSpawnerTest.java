package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.entities.npc.spawners.ScarecrowSpawner;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class ScarecrowSpawnerTest {

    @Test
    public void testConstructor() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        Assert.assertEquals("X should be set", 100, spawner.getX());
        Assert.assertEquals("Y should be set", 200, spawner.getY());
        Assert.assertNotNull("Timer should be initialized", spawner.getTimer());
    }

    @Test
    public void testGetTimer() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        engine.timing.TickTimer timer = spawner.getTimer();
        Assert.assertNotNull("Timer should not be null", timer);
    }

    @Test
    public void testGetX() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(300, 400);
        Assert.assertEquals("X should return 300", 300, spawner.getX());
    }

    @Test
    public void testGetY() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(300, 400);
        Assert.assertEquals("Y should return 400", 400, spawner.getY());
    }

    @Test
    public void testSetX() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        spawner.setX(500);
        Assert.assertEquals("X should be updated", 500, spawner.getX());
    }

    @Test
    public void testSetY() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        spawner.setY(600);
        Assert.assertEquals("Y should be updated", 600, spawner.getY());
    }

    @Test
    public void testTickCallsTimerTick() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        engine.timing.TickTimer timer = spawner.getTimer();
        Assert.assertFalse("Timer should not be finished initially", timer.isFinished());
        
        for (int i = 0; i < 300; i++) {
            spawner.tick(engine, game);
        }
        
        Assert.assertTrue("Timer should be finished after 300 ticks", timer.isFinished());
    }

    @Test
    public void testTickWithEnoughCoinsAndKeyPressed() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        TestGameState game = new TestGameState();
        game.inventory.setCoins(10);
        game.player.setX(250);
        game.player.setY(350);
        
        MockEngineState engine = new MockEngineState(new MockKeys('c'));
        
        int initialCoins = game.inventory.getCoins();
        int initialNpcs = game.npcs.npcs.size();
        
        spawner.tick(engine, game);
        
        Assert.assertEquals("Coins should be deducted", initialCoins - 2, game.inventory.getCoins());
        Assert.assertEquals("NPC should be added", initialNpcs + 1, game.npcs.npcs.size());
    }

    @Test
    public void testTickWithNotEnoughCoins() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        TestGameState game = new TestGameState();
        game.inventory.setCoins(1);
        
        MockEngineState engine = new MockEngineState(new MockKeys('c'));
        
        int initialNpcs = game.npcs.npcs.size();
        spawner.tick(engine, game);
        
        Assert.assertEquals("NPC should not be added", initialNpcs, game.npcs.npcs.size());
    }

    @Test
    public void testTickWithoutKeyPress() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        TestGameState game = new TestGameState();
        game.inventory.setCoins(10);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        
        int initialCoins = game.inventory.getCoins();
        spawner.tick(engine, game);
        
        Assert.assertEquals("Coins should not be deducted", initialCoins, game.inventory.getCoins());
    }

    @Test
    public void testTickMultipleTimes() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(100, 200);
        TestGameState game = new TestGameState();
        game.inventory.setCoins(20);
        
        MockEngineState engine = new MockEngineState(new MockKeys('c'));
        
        for (int i = 0; i < 5; i++) {
            spawner.tick(engine, game);
        }
        
        Assert.assertTrue("Multiple ticks should work", game.inventory.getCoins() <= 20);
    }

    @Test
    public void testGetXReturnsNonZero() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(500, 600);
        int x = spawner.getX();
        Assert.assertEquals("X should be 500", 500, x);
    }

    @Test
    public void testGetYReturnsNonZero() {
        ScarecrowSpawner spawner = new ScarecrowSpawner(500, 600);
        int y = spawner.getY();
        Assert.assertEquals("Y should be 600", 600, y);
    }

    public static class MockKeys implements engine.input.KeyState {
        private char key;
        
        public MockKeys(char key) {
            this.key = key;
        }
        
        public boolean isDown(char c) {
            return c == key;
        }
        
        public java.util.List<Character> getDown() {
            return java.util.List.of(key);
        }
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
        public void setCoins(int coins) { this.coins = coins; }
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

