package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.world.BeanWorld;
import builder.world.WorldBuilder;
import builder.entities.tiles.Dirt;
import builder.entities.tiles.Grass;
import builder.entities.tiles.Tile;
import builder.entities.resources.Cabbage;
import scenarios.mocks.MockEngineState;
import engine.renderer.TileGrid;

import java.util.List;

public class BeanWorldTest {

    @Test
    public void testAllTilesReturnsNonEmpty() {
        BeanWorld world = WorldBuilder.empty();
        Tile tile1 = new Dirt(0, 0);
        Tile tile2 = new Grass(100, 100);
        
        world.place(tile1);
        world.place(tile2);
        
        List<Tile> tiles = world.allTiles();
        Assert.assertEquals("Should return 2 tiles", 2, tiles.size());
        Assert.assertTrue("Should contain tile1", tiles.contains(tile1));
        Assert.assertTrue("Should contain tile2", tiles.contains(tile2));
    }

    @Test
    public void testAllTilesReturnsEmpty() {
        BeanWorld world = WorldBuilder.empty();
        List<Tile> tiles = world.allTiles();
        Assert.assertEquals("Should return empty list", 0, tiles.size());
    }

    @Test
    public void testRenderReturnsNonEmpty() {
        BeanWorld world = WorldBuilder.empty();
        Tile tile1 = new Dirt(0, 0);
        Tile tile2 = new Grass(100, 100);
        
        world.place(tile1);
        world.place(tile2);
        
        List<engine.renderer.Renderable> renderables = world.render();
        Assert.assertTrue("Should return non-empty list", renderables.size() >= 2);
    }

    @Test
    public void testRenderWithStackedEntities() {
        BeanWorld world = WorldBuilder.empty();
        Dirt tile = new Dirt(0, 0);
        Cabbage cabbage = new Cabbage(0, 0);
        tile.placeOn(cabbage);
        
        world.place(tile);
        
        List<engine.renderer.Renderable> renderables = world.render();
        Assert.assertTrue("Should return tile and cabbage", renderables.size() >= 2);
    }

    @Test
    public void testTickCallsTileTick() {
        BeanWorld world = WorldBuilder.empty();
        TestTile tile = new TestTile(0, 0);
        
        world.place(tile);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        Assert.assertFalse("Tile should not be ticked initially", tile.tickCalled);
        world.tick(engine, game);
        Assert.assertTrue("Tile should be ticked", tile.tickCalled);
    }

    @Test
    public void testTickMultipleTiles() {
        BeanWorld world = WorldBuilder.empty();
        TestTile tile1 = new TestTile(0, 0);
        TestTile tile2 = new TestTile(100, 100);
        
        world.place(tile1);
        world.place(tile2);
        
        MockEngineState engine = new MockEngineState(new TileGrid(25, 2000));
        TestGameState game = new TestGameState();
        
        world.tick(engine, game);
        
        Assert.assertTrue("Tile1 should be ticked", tile1.tickCalled);
        Assert.assertTrue("Tile2 should be ticked", tile2.tickCalled);
    }

    @Test
    public void testTileSelectorWithMatchingFilter() {
        BeanWorld world = WorldBuilder.empty();
        Dirt dirt = new Dirt(0, 0);
        Grass grass = new Grass(100, 100);
        
        world.place(dirt);
        world.place(grass);
        
        List<Tile> result = world.tileSelector(tile -> tile instanceof Dirt);
        Assert.assertEquals("Should return 1 Dirt tile", 1, result.size());
        Assert.assertTrue("Should contain dirt", result.contains(dirt));
    }

    @Test
    public void testTileSelectorWithNoMatch() {
        BeanWorld world = WorldBuilder.empty();
        Grass grass = new Grass(100, 100);
        
        world.place(grass);
        
        List<Tile> result = world.tileSelector(tile -> tile instanceof Dirt);
        Assert.assertEquals("Should return empty list", 0, result.size());
    }

    @Test
    public void testTileSelectorAllMatch() {
        BeanWorld world = WorldBuilder.empty();
        Dirt dirt1 = new Dirt(0, 0);
        Dirt dirt2 = new Dirt(100, 100);
        
        world.place(dirt1);
        world.place(dirt2);
        
        List<Tile> result = world.tileSelector(tile -> tile instanceof Dirt);
        Assert.assertEquals("Should return 2 tiles", 2, result.size());
    }

    @Test
    public void testTilesAtPositionFindsMatchingTile() {
        BeanWorld world = WorldBuilder.empty();
        TileGrid dimensions = new TileGrid(25, 2000);
        
        Tile tile = new Dirt(0, 0);
        world.place(tile);
        
        List<Tile> result = world.tilesAtPosition(10, 10, dimensions);
        Assert.assertEquals("Should find 1 tile", 1, result.size());
        Assert.assertTrue("Should contain the tile", result.contains(tile));
    }

    @Test
    public void testTilesAtPositionNoMatch() {
        BeanWorld world = WorldBuilder.empty();
        TileGrid dimensions = new TileGrid(25, 2000);
        
        Tile tile = new Dirt(0, 0);
        world.place(tile);
        
        List<Tile> result = world.tilesAtPosition(500, 500, dimensions);
        Assert.assertEquals("Should return empty list", 0, result.size());
    }

    @Test
    public void testTilesAtPositionMultipleTilesSamePosition() {
        BeanWorld world = WorldBuilder.empty();
        TileGrid dimensions = new TileGrid(25, 2000);
        
        Tile tile1 = new Dirt(0, 0);
        Tile tile2 = new Grass(10, 10);
        
        world.place(tile1);
        world.place(tile2);
        
        List<Tile> result = world.tilesAtPosition(5, 5, dimensions);
        Assert.assertTrue("Should find at least 1 tile", result.size() >= 1);
    }

    @Test
    public void testTilesAtPositionBoundaryConditions() {
        BeanWorld world = WorldBuilder.empty();
        TileGrid dimensions = new TileGrid(25, 2000);
        
        Tile tile1 = new Dirt(0, 0);
        Tile tile2 = new Grass(25, 25);
        Tile tile3 = new Dirt(50, 50);
        
        world.place(tile1);
        world.place(tile2);
        world.place(tile3);
        
        List<Tile> result1 = world.tilesAtPosition(0, 0, dimensions);
        Assert.assertTrue("Should find tile at (0,0)", result1.size() >= 1);
        
        List<Tile> result2 = world.tilesAtPosition(25, 25, dimensions);
        Assert.assertTrue("Should find tile at (25,25)", result2.size() >= 1);
    }

    @Test
    public void testTilesAtPositionExcludesOtherTiles() {
        BeanWorld world = WorldBuilder.empty();
        TileGrid dimensions = new TileGrid(25, 2000);
        
        Tile tileAtOrigin = new Dirt(0, 0);
        Tile tileAtFarLocation = new Grass(500, 500);
        
        world.place(tileAtOrigin);
        world.place(tileAtFarLocation);
        
        List<Tile> resultAtOrigin = world.tilesAtPosition(10, 10, dimensions);
        Assert.assertTrue("Should find tile at origin", resultAtOrigin.contains(tileAtOrigin));
        Assert.assertFalse("Should not include tile at far location", resultAtOrigin.contains(tileAtFarLocation));
        
        List<Tile> resultAtFar = world.tilesAtPosition(510, 510, dimensions);
        Assert.assertTrue("Should find tile at far location", resultAtFar.contains(tileAtFarLocation));
        Assert.assertFalse("Should not include tile at origin", resultAtFar.contains(tileAtOrigin));
    }

    @Test
    public void testPlace() {
        BeanWorld world = WorldBuilder.empty();
        Tile tile = new Dirt(0, 0);
        
        world.place(tile);
        
        List<Tile> tiles = world.allTiles();
        Assert.assertEquals("Should have 1 tile", 1, tiles.size());
        Assert.assertTrue("Should contain placed tile", tiles.contains(tile));
    }

    @Test
    public void testPlaceMultipleTiles() {
        BeanWorld world = WorldBuilder.empty();
        
        for (int i = 0; i < 10; i++) {
            world.place(new Dirt(i * 25, i * 25));
        }
        
        List<Tile> tiles = world.allTiles();
        Assert.assertEquals("Should have 10 tiles", 10, tiles.size());
    }

    public static class TestTile extends Grass {
        public boolean tickCalled = false;
        
        public TestTile(int x, int y) {
            super(x, y);
        }
        
        @Override
        public void tick(engine.EngineState state) {
            tickCalled = true;
            super.tick(state);
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
        
        public builder.world.World getWorld() { return WorldBuilder.empty(); }
        public builder.player.Player getPlayer() { return player; }
        public builder.inventory.Inventory getInventory() { return inventory; }
        public builder.entities.npc.NpcManager getNpcs() { return npcs; }
        public builder.entities.npc.enemies.EnemyManager getEnemies() { return enemies; }
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

