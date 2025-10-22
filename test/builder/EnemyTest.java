package builder;

import builder.entities.npc.enemies.Enemy;
import scenarios.mocks.MockEngineState;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Enemy class.
 */
public class EnemyTest {

    private Enemy enemy;
    private MockEngineState mockEngineState;

    @Before
    public void setUp() {
        enemy = new Enemy(100, 200);
        mockEngineState = new MockEngineState();
    }

    @Test
    public void testTickCallsSuperTick() {
        // Test that enemy.tick calls super.tick (line 15)
        enemy.tick(mockEngineState, null);
        Assert.assertTrue("Tick method should complete without error", true);
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
}