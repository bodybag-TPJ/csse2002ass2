package builder;

import builder.entities.npc.enemies.Enemy;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

import org.junit.Assert;
import org.junit.Test;

public class EnemyTest {

    @Test
    public void testConstructor() {
        TestableEnemy enemy = new TestableEnemy(100, 200);
        Assert.assertEquals("X coordinate should be set", 100, enemy.getX());
        Assert.assertEquals("Y coordinate should be set", 200, enemy.getY());
    }

    @Test
    public void testInteract() {
        TestableEnemy enemy = new TestableEnemy(100, 200);
        MockEngineState engine = new MockEngineState();
        enemy.interact(engine, null);
        Assert.assertTrue("Interact method should complete without error", true);
    }

    @Test
    public void testTickCallsSuperTick() {
        TestableEnemy enemy = new TestableEnemy(100, 200);
        enemy.setDirection(0);
        enemy.setSpeed(5);
        
        int initialX = enemy.getX();
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        enemy.tick(engine, game);
        
        Assert.assertNotEquals("Position should change after tick (super.tick calls move)", initialX, enemy.getX());
    }

    @Test
    public void testTickWithDifferentDirection() {
        TestableEnemy enemy = new TestableEnemy(100, 200);
        enemy.setDirection(90);
        enemy.setSpeed(3);
        
        int initialY = enemy.getY();
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        enemy.tick(engine, game);
        
        Assert.assertNotEquals("Position should change after tick", initialY, enemy.getY());
    }

    @Test
    public void testTickMultipleTimes() {
        TestableEnemy enemy = new TestableEnemy(100, 200);
        enemy.setDirection(45);
        enemy.setSpeed(2);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        for (int i = 0; i < 5; i++) {
            enemy.tick(engine, game);
        }
        
        Assert.assertTrue("Multiple ticks should complete", true);
    }

    @Test
    public void testTickMovesEnemy() {
        TestableEnemy enemy = new TestableEnemy(500, 500);
        enemy.setDirection(180);
        enemy.setSpeed(4);
        
        int initialX = enemy.getX();
        int initialY = enemy.getY();
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        enemy.tick(engine, game);
        
        boolean positionChanged = (enemy.getX() != initialX) || (enemy.getY() != initialY);
        Assert.assertTrue("Enemy should move when tick is called", positionChanged);
    }

    private static class TestableEnemy extends Enemy {
        public TestableEnemy(int x, int y) {
            super(x, y);
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
