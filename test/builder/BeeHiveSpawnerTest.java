package builder;

import builder.entities.npc.spawners.BeeHiveSpawner;
import engine.timing.TickTimer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for BeeHiveSpawner class to cover all mutation testing scenarios.
 */
public class BeeHiveSpawnerTest {

    private BeeHiveSpawner spawner;

    @Before
    public void setUp() {
        spawner = new BeeHiveSpawner(100, 200, 300);
    }

    /**
     * Test getTimer() method returns non-null timer
     * Covers mutation: replaced return value with null for getTimer
     */
    @Test
    public void testGetTimerReturnsNonNull() {
        TickTimer timer = spawner.getTimer();
        Assert.assertNotNull("Timer should not be null", timer);
    }

    /**
     * Test getX() method returns correct X coordinate
     * Covers mutation: replaced int return with 0 for getX
     */
    @Test
    public void testGetXReturnsCorrectValue() {
        Assert.assertEquals("X coordinate should be 100", 100, spawner.getX());
        
        spawner.setX(250);
        Assert.assertEquals("X coordinate should be 250 after setting", 250, spawner.getX());
        
        spawner.setX(0);
        Assert.assertEquals("X coordinate should be 0 when explicitly set to 0", 0, spawner.getX());
        
        spawner.setX(-50);
        Assert.assertEquals("X coordinate should be -50", -50, spawner.getX());
    }

    /**
     * Test getY() method returns correct Y coordinate
     * Covers mutation: replaced int return with 0 for getY
     */
    @Test
    public void testGetYReturnsCorrectValue() {
        Assert.assertEquals("Y coordinate should be 200", 200, spawner.getY());
        
        spawner.setY(350);
        Assert.assertEquals("Y coordinate should be 350 after setting", 350, spawner.getY());
        
        spawner.setY(0);
        Assert.assertEquals("Y coordinate should be 0 when explicitly set to 0", 0, spawner.getY());
        
        spawner.setY(-75);
        Assert.assertEquals("Y coordinate should be -75", -75, spawner.getY());
    }

    /**
     * Test setX() method
     */
    @Test
    public void testSetX() {
        spawner.setX(500);
        Assert.assertEquals("X should be set to 500", 500, spawner.getX());
    }

    /**
     * Test setY() method
     */
    @Test
    public void testSetY() {
        spawner.setY(600);
        Assert.assertEquals("Y should be set to 600", 600, spawner.getY());
    }

    /**
     * Test constructor sets coordinates correctly
     */
    @Test
    public void testConstructor() {
        BeeHiveSpawner newSpawner = new BeeHiveSpawner(50, 75, 500);
        Assert.assertEquals("X coordinate should be set correctly", 50, newSpawner.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 75, newSpawner.getY());
        Assert.assertNotNull("Timer should be initialized", newSpawner.getTimer());
    }

    /**
     * Test that tick method can be called without throwing exceptions
     * This covers the basic tick functionality including timer.tick() call
     */
    @Test
    public void testTickMethodBasic() {
        // Create a minimal mock for testing
        TestEngineState engineState = new TestEngineState();
        TestGameState gameState = new TestGameState();
        
        // Should not throw any exceptions
        try {
            spawner.tick(engineState, gameState);
            Assert.assertTrue("Tick method should complete without exceptions", true);
        } catch (Exception e) {
            Assert.fail("Tick method should not throw exceptions: " + e.getMessage());
        }
    }

    // Minimal test implementations that focus only on what BeeHiveSpawner needs
    private static class TestEngineState implements engine.EngineState {
        private boolean hKeyPressed = false;
        
        public void setHKeyPressed(boolean pressed) {
            this.hKeyPressed = pressed;
        }
        
        @Override
        public engine.input.KeyState getKeys() {
            return new engine.input.KeyState() {
                @Override
                public boolean isDown(char key) {
                    return key == 'h' && hKeyPressed;
                }
                
                @Override
                public java.util.List<Character> getDown() {
                    java.util.List<Character> downKeys = new java.util.ArrayList<>();
                    if (hKeyPressed) {
                        downKeys.add('h');
                    }
                    return downKeys;
                }
            };
        }
        
        @Override
        public engine.input.MouseState getMouse() { 
            return null; 
        }
        
        @Override
        public engine.renderer.Dimensions getDimensions() { 
            return null; 
        }
        
        @Override
        public int currentTick() { 
            return 0; 
        }
    }

    private static class TestGameState implements builder.GameState {
        private TestInventory inventory = new TestInventory();
        private TestPlayer player = new TestPlayer();
        private builder.entities.npc.NpcManager npcs = new builder.entities.npc.NpcManager();
        private builder.entities.npc.enemies.EnemyManager enemies = new builder.entities.npc.enemies.EnemyManager(null);

        @Override
        public builder.world.World getWorld() { 
            return null; 
        }

        @Override
        public builder.entities.npc.NpcManager getNpcs() { 
            return npcs; 
        }

        @Override
        public builder.entities.npc.enemies.EnemyManager getEnemies() { 
            return enemies; 
        }

        @Override
        public builder.player.Player getPlayer() { 
            return player; 
        }

        @Override
        public builder.inventory.Inventory getInventory() { 
            return inventory; 
        }
    }

    private static class TestInventory implements builder.inventory.Inventory {
        private int food = 0;
        private int coins = 0;

        @Override
        public void addFood(int amount) { 
            this.food = Math.max(0, this.food + amount); 
        }

        @Override
        public int getFood() { 
            return food; 
        }

        @Override
        public void addCoins(int amount) { 
            this.coins = Math.max(0, this.coins + amount); 
        }

        @Override
        public int getCoins() { 
            return coins; 
        }

        @Override
        public int getCapacity() { 
            return 10; 
        }

        // Stub implementations for other required methods
        @Override
        public void setItem(int slot, builder.inventory.items.Item item) {}
        
        @Override
        public builder.inventory.items.Item getItem(int slot) { 
            return null; 
        }
        
        @Override
        public builder.inventory.items.Item getHolding() { 
            return null; 
        }
        
        @Override
        public int getActiveSlot() { 
            return 0; 
        }
        
        @Override
        public void setActiveSlot(int slot) {}

        // Helper methods for testing
        public void setFood(int food) { 
            this.food = Math.max(0, food); 
        }
        
        public void setCoins(int coins) { 
            this.coins = Math.max(0, coins); 
        }
    }

    private static class TestPlayer implements builder.player.Player {
        private int x = 100, y = 200;

        @Override
        public int getX() { 
            return x; 
        }

        @Override
        public int getY() { 
            return y; 
        }

        public java.util.UUID getUUID() { 
            return java.util.UUID.randomUUID(); 
        }
        
        @Override
        public String getID() {
            return "test-player";
        }
        
        @Override
        public int getDamage() { 
            return 0; 
        }

        public void setX(int x) { 
            this.x = x; 
        }
        
        public void setY(int y) { 
            this.y = y; 
        }
    }
}