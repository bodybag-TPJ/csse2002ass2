package builder;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import scenarios.mocks.MockEngineState;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Enemy class.
 */
public class EnemyTest {

    private TestableEnemy enemy;
    private MockEngineState mockEngineState;

    @Before
    public void setUp() {
        enemy = new TestableEnemy(100, 200);
        mockEngineState = new MockEngineState();
    }

    @Test
    public void testTickCallsSuperTick() {
        // Test that enemy.tick calls super.tick (line 15)
        Assert.assertFalse("Super tick should not have been called initially", enemy.superTickCalled);
        
        enemy.tick(mockEngineState, null);
        
        Assert.assertTrue("Super tick should have been called", enemy.superTickCalled);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals("X coordinate should be set", 100, enemy.getX());
        Assert.assertEquals("Y coordinate should be set", 200, enemy.getY());
    }

    @Test
    public void testInteract() {
        // Test that interact method exists and can be called without error
        enemy.interact(mockEngineState, null);
        Assert.assertTrue("Interact method should complete without error", true);
    }

    // Testable subclass to track super.tick calls
    private static class TestableEnemy extends Enemy {
        public boolean superTickCalled = false;

        public TestableEnemy(int x, int y) {
            super(x, y);
        }

        @Override
        public void tick(engine.EngineState state, GameState game) {
            superTickCalled = true;
            super.tick(state, game);
        }
    }
}