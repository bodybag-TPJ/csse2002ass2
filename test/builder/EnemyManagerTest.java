package builder;

import builder.entities.npc.enemies.*;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.TickTimer;
import org.junit.Assert;
import org.junit.Test;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

import java.util.List;

public class EnemyManagerTest {

    @Test
    public void testConstructor() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        Assert.assertNotNull("EnemyManager should be created", manager);
        Assert.assertEquals("Should have no enemies initially", 0, manager.getBirds().size());
    }

    @Test
    public void testCleanupRemovesMarkedEnemies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, false);
        TestEnemy enemy2 = new TestEnemy(200, 200, true);
        TestEnemy enemy3 = new TestEnemy(300, 300, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        manager.getBirds().add(enemy3);
        
        Assert.assertEquals("Should have 3 enemies", 3, manager.getBirds().size());
        
        manager.cleanup();
        
        Assert.assertEquals("Should have 2 enemies after cleanup", 2, manager.getBirds().size());
        Assert.assertTrue("Should contain enemy1", manager.getBirds().contains(enemy1));
        Assert.assertFalse("Should not contain enemy2", manager.getBirds().contains(enemy2));
        Assert.assertTrue("Should contain enemy3", manager.getBirds().contains(enemy3));
    }

    @Test
    public void testCleanupWithMultipleMarkedEnemies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, true);
        TestEnemy enemy2 = new TestEnemy(200, 200, true);
        TestEnemy enemy3 = new TestEnemy(300, 300, true);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        manager.getBirds().add(enemy3);
        
        manager.cleanup();
        
        Assert.assertEquals("All marked enemies should be removed", 0, manager.getBirds().size());
    }

    @Test
    public void testCleanupWithNoMarkedEnemies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, false);
        TestEnemy enemy2 = new TestEnemy(200, 200, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        
        manager.cleanup();
        
        Assert.assertEquals("No enemies should be removed", 2, manager.getBirds().size());
    }

    @Test
    public void testCleanupBackwardsIteration() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, true);
        TestEnemy enemy2 = new TestEnemy(200, 200, false);
        TestEnemy enemy3 = new TestEnemy(300, 300, true);
        TestEnemy enemy4 = new TestEnemy(400, 400, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        manager.getBirds().add(enemy3);
        manager.getBirds().add(enemy4);
        
        manager.cleanup();
        
        Assert.assertEquals("Should have 2 enemies", 2, manager.getBirds().size());
        Assert.assertEquals("First should be enemy2", enemy2, manager.getBirds().get(0));
        Assert.assertEquals("Second should be enemy4", enemy4, manager.getBirds().get(1));
    }

    @Test
    public void testAddSpawner() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestSpawner spawner = new TestSpawner();
        manager.add(spawner);
        
        // Verify spawner is added by ticking
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        manager.tick(engine, game);
        
        Assert.assertTrue("Spawner should be ticked", spawner.tickCalled);
    }

    @Test
    public void testMkMCreatesAndAddsMagpie() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        manager.setSpawnX(150);
        manager.setSpawnY(250);
        
        TestPlayer player = new TestPlayer();
        
        Magpie magpie = manager.mkM(player);
        
        Assert.assertNotNull("Magpie should be created", magpie);
        Assert.assertEquals("Magpie should have correct X", 150, magpie.getX());
        Assert.assertEquals("Magpie should have correct Y", 250, magpie.getY());
        Assert.assertEquals("Birds list should have 1 enemy", 1, manager.getBirds().size());
        Assert.assertTrue("Birds should contain the magpie", manager.getBirds().contains(magpie));
    }

    @Test
    public void testMkECreatesAndAddsEagle() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        manager.setSpawnX(175);
        manager.setSpawnY(275);
        
        TestPlayer player = new TestPlayer();
        
        Eagle eagle = manager.mkE(player);
        
        Assert.assertNotNull("Eagle should be created", eagle);
        Assert.assertEquals("Eagle should have correct X", 175, eagle.getX());
        Assert.assertEquals("Eagle should have correct Y", 275, eagle.getY());
        Assert.assertEquals("Birds list should have 1 enemy", 1, manager.getBirds().size());
        Assert.assertTrue("Birds should contain the eagle", manager.getBirds().contains(eagle));
    }

    @Test
    public void testMkPCreatesAndAddsPigeon() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        manager.setSpawnX(200);
        manager.setSpawnY(300);
        
        TestHasPosition target = new TestHasPosition(500, 600);
        
        Pigeon pigeon = manager.mkP(target);
        
        Assert.assertNotNull("Pigeon should be created", pigeon);
        Assert.assertEquals("Pigeon should have correct X", 200, pigeon.getX());
        Assert.assertEquals("Pigeon should have correct Y", 300, pigeon.getY());
        Assert.assertEquals("Birds list should have 1 enemy", 1, manager.getBirds().size());
        Assert.assertTrue("Birds should contain the pigeon", manager.getBirds().contains(pigeon));
    }

    @Test
    public void testGetAllReturnsEnemyList() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, false);
        TestEnemy enemy2 = new TestEnemy(200, 200, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        
        List<Enemy> allEnemies = manager.getAll();
        
        Assert.assertNotNull("Should return non-null list", allEnemies);
        Assert.assertEquals("Should return all enemies", 2, allEnemies.size());
        Assert.assertTrue("Should contain enemy1", allEnemies.contains(enemy1));
        Assert.assertTrue("Should contain enemy2", allEnemies.contains(enemy2));
    }

    @Test
    public void testGetMagpiesReturnsOnlyMagpies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        TestPlayer player = new TestPlayer();
        TestHasPosition target = new TestHasPosition(500, 600);
        
        manager.setSpawnX(100);
        manager.setSpawnY(100);
        Magpie magpie1 = manager.mkM(player);
        
        manager.setSpawnX(200);
        manager.setSpawnY(200);
        manager.mkE(player);
        
        manager.setSpawnX(300);
        manager.setSpawnY(300);
        Magpie magpie2 = manager.mkM(player);
        
        manager.setSpawnX(400);
        manager.setSpawnY(400);
        manager.mkP(target);
        
        List<Magpie> magpies = manager.getMagpies();
        
        Assert.assertNotNull("Should return non-null list", magpies);
        Assert.assertEquals("Should return only magpies", 2, magpies.size());
        Assert.assertTrue("Should contain magpie1", magpies.contains(magpie1));
        Assert.assertTrue("Should contain magpie2", magpies.contains(magpie2));
    }

    @Test
    public void testGetMagpiesReturnsEmptyListWhenNoMagpies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        TestPlayer player = new TestPlayer();
        
        manager.setSpawnX(100);
        manager.setSpawnY(100);
        manager.mkE(player);
        
        List<Magpie> magpies = manager.getMagpies();
        
        Assert.assertNotNull("Should return non-null list", magpies);
        Assert.assertEquals("Should return empty list", 0, magpies.size());
    }

    @Test
    public void testTickCallsCleanup() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestEnemy markedEnemy = new TestEnemy(100, 100, true);
        TestEnemy normalEnemy = new TestEnemy(200, 200, false);
        
        manager.getBirds().add(markedEnemy);
        manager.getBirds().add(normalEnemy);
        
        manager.tick(engine, game);
        
        Assert.assertEquals("Cleanup should have removed marked enemy", 1, manager.getBirds().size());
        Assert.assertTrue("Should only contain normal enemy", manager.getBirds().contains(normalEnemy));
    }

    @Test
    public void testTickCallsSpawnerTick() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestSpawner spawner1 = new TestSpawner();
        TestSpawner spawner2 = new TestSpawner();
        
        manager.add(spawner1);
        manager.add(spawner2);
        
        manager.tick(engine, game);
        
        Assert.assertTrue("Spawner1 tick should be called", spawner1.tickCalled);
        Assert.assertTrue("Spawner2 tick should be called", spawner2.tickCalled);
    }

    @Test
    public void testTickCallsEnemyTick() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        TestEnemy enemy1 = new TestEnemy(100, 100, false);
        TestEnemy enemy2 = new TestEnemy(200, 200, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        
        manager.tick(engine, game);
        
        Assert.assertTrue("Enemy1 tick should be called", enemy1.tickCalled);
        Assert.assertTrue("Enemy2 tick should be called", enemy2.tickCalled);
    }

    @Test
    public void testRenderReturnsAllEnemies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        TestEnemy enemy1 = new TestEnemy(100, 100, false);
        TestEnemy enemy2 = new TestEnemy(200, 200, false);
        
        manager.getBirds().add(enemy1);
        manager.getBirds().add(enemy2);
        
        List<?> renderables = manager.render();
        
        Assert.assertNotNull("Should return non-null list", renderables);
        Assert.assertEquals("Should return all enemies", 2, renderables.size());
    }

    @Test
    public void testRenderReturnsEmptyListWhenNoEnemies() {
        TileGrid dimensions = new TileGrid(25, 2000);
        EnemyManager manager = new EnemyManager(dimensions);
        
        List<?> renderables = manager.render();
        
        Assert.assertNotNull("Should return non-null list", renderables);
        Assert.assertEquals("Should return empty list", 0, renderables.size());
    }

    // Test helper classes
    private static class TestEnemy extends Enemy {
        private final boolean markedForRemoval;
        public boolean tickCalled = false;

        public TestEnemy(int x, int y, boolean markedForRemoval) {
            super(x, y);
            this.markedForRemoval = markedForRemoval;
        }

        @Override
        public boolean isMarkedForRemoval() {
            return markedForRemoval;
        }

        @Override
        public void tick(EngineState state, GameState game) {
            tickCalled = true;
        }
    }

    private static class TestSpawner implements Spawner {
        public boolean tickCalled = false;

        @Override
        public TickTimer getTimer() {
            return null;
        }

        @Override
        public void tick(EngineState state, GameState game) {
            tickCalled = true;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public void setX(int x) {
        }

        @Override
        public int getY() {
            return 0;
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
        public Player getPlayer() {
            return null;
        }

        @Override
        public builder.inventory.Inventory getInventory() {
            return null;
        }

        @Override
        public EnemyManager getEnemies() {
            return null;
        }

        @Override
        public builder.entities.npc.NpcManager getNpcs() {
            return null;
        }
    }

    private static class TestPlayer implements Player {
        @Override
        public int getX() {
            return 100;
        }

        @Override
        public int getY() {
            return 100;
        }

        @Override
        public void setX(int x) {
        }

        @Override
        public void setY(int y) {
        }

        @Override
        public String getID() {
            return "test-player";
        }

        @Override
        public int getDamage() {
            return 1;
        }
    }

    private static class TestHasPosition implements HasPosition {
        private final int x;
        private final int y;

        public TestHasPosition(int x, int y) {
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
}

