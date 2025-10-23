package builder;

import builder.entities.npc.Npc;
import engine.game.HasPosition;
import org.junit.Assert;
import org.junit.Test;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

public class NpcTest {

    @Test
    public void testGetSpeed() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setSpeed(5);
        Assert.assertEquals("Speed should be 5", 5.0, npc.getSpeed(), 0.01);
    }

    @Test
    public void testGetSpeedNonZero() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setSpeed(3);
        double speed = npc.getSpeed();
        Assert.assertNotEquals("Speed should not be 0", 0.0, speed, 0.01);
        Assert.assertEquals("Speed should be 3", 3.0, speed, 0.01);
    }

    @Test
    public void testGetDirection() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(90);
        Assert.assertEquals("Direction should be 90", 90, npc.getDirection());
    }

    @Test
    public void testGetDirectionNonZero() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(45);
        int direction = npc.getDirection();
        Assert.assertNotEquals("Direction should not be 0", 0, direction);
        Assert.assertEquals("Direction should be 45", 45, direction);
    }

    @Test
    public void testMoveWithPositiveDirection() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(0); // Move right
        npc.setSpeed(10);
        
        int initialX = npc.getX();
        int initialY = npc.getY();
        
        npc.move();
        
        // Direction 0 degrees = move right (positive X)
        Assert.assertTrue("X should increase", npc.getX() > initialX);
        Assert.assertEquals("Y should stay same", initialY, npc.getY());
    }

    @Test
    public void testMoveWithDirection90() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(90); // Move down
        npc.setSpeed(10);
        
        int initialX = npc.getX();
        int initialY = npc.getY();
        
        npc.move();
        
        // Direction 90 degrees = move down (positive Y)
        Assert.assertEquals("X should stay same", initialX, npc.getX());
        Assert.assertTrue("Y should increase", npc.getY() > initialY);
    }

    @Test
    public void testMoveChangesPosition() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(45);
        npc.setSpeed(10);
        
        int initialX = npc.getX();
        int initialY = npc.getY();
        
        npc.move();
        
        int newX = npc.getX();
        int newY = npc.getY();
        
        // If addition is replaced with subtraction, position would decrease instead
        Assert.assertNotEquals("Position should change", initialX, newX);
        Assert.assertNotEquals("Position should change", initialY, newY);
    }

    @Test
    public void testTickCallsMove() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(0);
        npc.setSpeed(5);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        
        int initialX = npc.getX();
        
        npc.tick(engine);
        
        Assert.assertNotEquals("tick should call move and change position", initialX, npc.getX());
    }

    @Test
    public void testTickWithGameStateCallsMove() {
        TestNpc npc = new TestNpc(100, 200);
        npc.setDirection(90);
        npc.setSpeed(5);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        int initialY = npc.getY();
        
        npc.tick(engine, game);
        
        Assert.assertNotEquals("tick should call move and change position", initialY, npc.getY());
    }

    @Test
    public void testDistanceFromPosition() {
        TestNpc npc = new TestNpc(100, 200);
        TestPosition target = new TestPosition(103, 204);
        
        int distance = npc.distanceFrom(target);
        
        // Distance = sqrt((103-100)^2 + (204-200)^2) = sqrt(9+16) = sqrt(25) = 5
        Assert.assertEquals("Distance should be 5", 5, distance);
    }

    @Test
    public void testDistanceFromCoordinates() {
        TestNpc npc = new TestNpc(100, 200);
        
        int distance = npc.distanceFrom(103, 204);
        
        // Distance = sqrt((103-100)^2 + (204-200)^2) = sqrt(9+16) = sqrt(25) = 5
        Assert.assertEquals("Distance should be 5", 5, distance);
    }

    @Test
    public void testDistanceFromCoordinatesNegativeDelta() {
        TestNpc npc = new TestNpc(100, 200);
        
        int distance = npc.distanceFrom(97, 196);
        
        // Distance = sqrt((97-100)^2 + (196-200)^2) = sqrt(9+16) = sqrt(25) = 5
        Assert.assertEquals("Distance should be 5", 5, distance);
    }

    @Test
    public void testDistanceFromCoordinatesSubtractionNotAddition() {
        TestNpc npc = new TestNpc(100, 200);
        
        // If x - getX() is changed to x + getX(), distance would be very different
        int distance1 = npc.distanceFrom(103, 204);
        
        // Expected: sqrt((103-100)^2 + (204-200)^2) = 5
        // If mutation: sqrt((103+100)^2 + (204+200)^2) = sqrt(41209+163216) = 452
        Assert.assertTrue("Distance should be small (< 10)", distance1 < 10);
        Assert.assertEquals("Distance should be exactly 5", 5, distance1);
    }

    @Test
    public void testDistanceFromCoordinatesAdditionNotSubtraction() {
        TestNpc npc = new TestNpc(100, 200);
        
        int distance = npc.distanceFrom(106, 208);
        
        // Distance = sqrt((6)^2 + (8)^2) = sqrt(36+64) = sqrt(100) = 10
        // If addition changed to subtraction: sqrt(36-64) = sqrt(-28) -> invalid or wrong
        Assert.assertEquals("Distance should be 10", 10, distance);
    }

    @Test
    public void testDistanceFromPositionCalculation() {
        TestNpc npc = new TestNpc(0, 0);
        TestPosition target = new TestPosition(3, 4);
        
        int distance = npc.distanceFrom(target);
        
        // Distance = sqrt(9 + 16) = 5
        // If deltaX or deltaY subtraction is changed to addition, result would be wrong
        Assert.assertEquals("Distance should be 5", 5, distance);
    }

    // Test helper classes
    private static class TestNpc extends Npc {
        public TestNpc(int x, int y) {
            super(x, y);
        }
    }

    private static class TestPosition implements HasPosition {
        private final int x;
        private final int y;

        public TestPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

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
        }

        @Override
        public void setY(int y) {
        }
    }

    private static class TestGameState implements GameState {
        @Override
        public builder.world.World getWorld() {
            return null;
        }

        @Override
        public builder.player.Player getPlayer() {
            return null;
        }

        @Override
        public builder.inventory.Inventory getInventory() {
            return null;
        }

        @Override
        public builder.entities.npc.enemies.EnemyManager getEnemies() {
            return null;
        }

        @Override
        public builder.entities.npc.NpcManager getNpcs() {
            return null;
        }
    }
}

