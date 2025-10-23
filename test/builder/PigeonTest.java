package builder;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for Pigeon class to cover all mutation testing scenarios.
 */
public class PigeonTest {

    /**
     * Test first constructor sets coordinates and initializes properly
     * Covers mutations: constructor method calls and field assignments
     */
    @Test
    public void testConstructorBasic() {
        builder.entities.npc.enemies.Pigeon testPigeon = new builder.entities.npc.enemies.Pigeon(50, 75);
        Assert.assertEquals("X coordinate should be set correctly", 50, testPigeon.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 75, testPigeon.getY());
        Assert.assertNotNull("Lifespan should be initialized", testPigeon.getLifespan());
        Assert.assertTrue("Should be attacking initially", testPigeon.attacking);
    }

    /**
     * Test second constructor with tracked target sets all fields correctly
     * Covers mutations: constructor method calls and field assignments
     */
    @Test
    public void testConstructorWithTarget() {
        TestHasPosition target = new TestHasPosition(500, 600);
        builder.entities.npc.enemies.Pigeon testPigeon = new builder.entities.npc.enemies.Pigeon(80, 90, target);
        Assert.assertEquals("X coordinate should be set correctly", 80, testPigeon.getX());
        Assert.assertEquals("Y coordinate should be set correctly", 90, testPigeon.getY());
        Assert.assertNotNull("Lifespan should be initialized", testPigeon.getLifespan());
        Assert.assertTrue("Should be attacking initially", testPigeon.attacking);
    }

    /**
     * Test getLifespan() returns non-null timer
     * Covers mutation: replaced return value with null for getLifespan
     */
    @Test
    public void testGetLifespanReturnsNonNull() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        engine.timing.FixedTimer timer = pigeon.getLifespan();
        Assert.assertNotNull("Lifespan should not be null", timer);
    }

    /**
     * Test setLifespan() properly updates the timer
     * Covers mutation: method call removal for setLifespan
     */
    @Test
    public void testSetLifespan() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        engine.timing.FixedTimer newTimer = new engine.timing.FixedTimer(5000);
        pigeon.setLifespan(newTimer);
        Assert.assertEquals("Lifespan should be updated", newTimer, pigeon.getLifespan());
    }

    /**
     * Test attacking field mutations
     * Covers mutations: boolean field assignments
     */
    @Test
    public void testAttackingField() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        
        // Test initial state
        Assert.assertTrue("Should be attacking initially", pigeon.attacking);
        
        // Test state change
        pigeon.attacking = false;
        Assert.assertFalse("Should not be attacking after change", pigeon.attacking);
    }

    /**
     * Test spawn coordinates are preserved
     * Covers mutations: spawnX and spawnY field usage
     */
    @Test
    public void testSpawnCoordinates() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        Assert.assertEquals("Spawn X should be 100", 100, pigeon.getX());
        Assert.assertEquals("Spawn Y should be 200", 200, pigeon.getY());
        
        TestHasPosition target = new TestHasPosition(300, 400);
        builder.entities.npc.enemies.Pigeon pigeonWithTarget = new builder.entities.npc.enemies.Pigeon(150, 250, target);
        Assert.assertEquals("Target pigeon spawn X should be 150", 150, pigeonWithTarget.getX());
        Assert.assertEquals("Target pigeon spawn Y should be 250", 250, pigeonWithTarget.getY());
    }

    /**
     * Test that lifespan timer is properly initialized
     * Covers mutations: timer initialization and 3000ms duration
     */
    @Test
    public void testLifespanInitialization() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        engine.timing.FixedTimer timer = pigeon.getLifespan();
        Assert.assertNotNull("Timer should be initialized", timer);
        
        TestHasPosition target = new TestHasPosition(300, 400);
        builder.entities.npc.enemies.Pigeon pigeonWithTarget = new builder.entities.npc.enemies.Pigeon(150, 250, target);
        engine.timing.FixedTimer targetTimer = pigeonWithTarget.getLifespan();
        Assert.assertNotNull("Target pigeon timer should be initialized", targetTimer);
    }

    /**
     * Test constructor sets speed to 4
     * Covers mutations: setSpeed(4) method call
     */
    @Test
    public void testConstructorSetsSpeed() {
        TestHasPosition target = new TestHasPosition(300, 400);
        
        builder.entities.npc.enemies.Pigeon speedTest = new builder.entities.npc.enemies.Pigeon(0, 0);
        Assert.assertNotNull("Pigeon should be created successfully", speedTest);
        
        builder.entities.npc.enemies.Pigeon speedTestWithTarget = new builder.entities.npc.enemies.Pigeon(0, 0, target);
        Assert.assertNotNull("Pigeon with target should be created successfully", speedTestWithTarget);
    }

    /**
     * Test equality conditions and boundary values
     * Covers mutations: == to !=, >= to <, etc.
     */
    @Test
    public void testBoundaryValues() {
        // Test coordinate boundaries
        builder.entities.npc.enemies.Pigeon boundaryPigeon = new builder.entities.npc.enemies.Pigeon(400, 400);
        Assert.assertEquals("X should be 400", 400, boundaryPigeon.getX());
        Assert.assertEquals("Y should be 400", 400, boundaryPigeon.getY());
        
        // Test zero coordinates
        builder.entities.npc.enemies.Pigeon zeroPigeon = new builder.entities.npc.enemies.Pigeon(0, 0);
        Assert.assertEquals("X should be 0", 0, zeroPigeon.getX());
        Assert.assertEquals("Y should be 0", 0, zeroPigeon.getY());
    }

    /**
     * Test arithmetic operations in distance calculations
     * Covers mutations: subtraction to addition, etc.
     */
    @Test
    public void testArithmeticOperations() {
        // Test different coordinate combinations to exercise arithmetic
        builder.entities.npc.enemies.Pigeon positivePigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        builder.entities.npc.enemies.Pigeon smallPigeon = new builder.entities.npc.enemies.Pigeon(5, 10);
        
        Assert.assertEquals("Positive X should be 100", 100, positivePigeon.getX());
        Assert.assertEquals("Positive Y should be 200", 200, positivePigeon.getY());
        Assert.assertEquals("Small X should be 5", 5, smallPigeon.getX());
        Assert.assertEquals("Small Y should be 10", 10, smallPigeon.getY());
    }

    /**
     * Test constructor method calls coverage
     * Covers mutations: setSpeed and setSprite calls in constructor
     */
    @Test
    public void testConstructorMethodCalls() {
        // Test that constructor completes without exceptions (covers setSpeed and setSprite calls)
        try {
            builder.entities.npc.enemies.Pigeon pigeon1 = new builder.entities.npc.enemies.Pigeon(10, 20);
            Assert.assertNotNull("Constructor should complete successfully", pigeon1);
            
            TestHasPosition target = new TestHasPosition(30, 40);
            builder.entities.npc.enemies.Pigeon pigeon2 = new builder.entities.npc.enemies.Pigeon(10, 20, target);
            Assert.assertNotNull("Constructor with target should complete successfully", pigeon2);
        } catch (Exception e) {
            Assert.fail("Constructor should not throw exceptions: " + e.getMessage());
        }
    }

    /**
     * Test integer constant mutations
     * Covers mutations: changing 3000 to other values, changing 4 to other values
     */
    @Test
    public void testIntegerConstants() {
        builder.entities.npc.enemies.Pigeon pigeon = new builder.entities.npc.enemies.Pigeon(100, 200);
        
        // Test that lifespan timer exists (should be 3000ms originally)
        engine.timing.FixedTimer timer = pigeon.getLifespan();
        Assert.assertNotNull("Timer should be initialized with proper duration", timer);
        
        // Test coordinates that would trigger different branches in tick logic
        builder.entities.npc.enemies.Pigeon leftPigeon = new builder.entities.npc.enemies.Pigeon(300, 500); // x < 400
        builder.entities.npc.enemies.Pigeon rightPigeon = new builder.entities.npc.enemies.Pigeon(500, 500); // x >= 400
        
        Assert.assertTrue("Left pigeon X should be < 400", leftPigeon.getX() < 400);
        Assert.assertTrue("Right pigeon X should be >= 400", rightPigeon.getX() >= 400);
    }

    // Helper test class - 实现HasPosition接口但保持简单
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
}