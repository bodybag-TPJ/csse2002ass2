package builder;

import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.spawners.EagleSpawner;
import builder.entities.npc.spawners.MagpieSpawner;
import builder.entities.npc.spawners.PigeonSpawner;
import builder.entities.tiles.Dirt;
import builder.entities.tiles.Tile;
import builder.inventory.*;
import builder.inventory.items.Bucket;
import builder.inventory.items.HiveHammer;
import builder.inventory.items.Hoe;
import builder.inventory.items.Jackhammer;
import builder.inventory.items.Pole;
import builder.inventory.ui.InventoryOverlay;
import builder.inventory.ui.ResourceOverlay;
import builder.player.PlayerManager;
import builder.ui.Overlay;
import builder.world.BeanWorld;
import builder.world.CabbageDetails;
import builder.world.OverlayBuilder;
import builder.world.PlayerDetails;
import builder.world.SpawnerDetails;
import builder.world.WorldBuilder;
import builder.world.WorldLoadException;

import engine.EngineState;
import engine.game.Game;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * JavaBeans, a farming game.
 *
 * <p>In this game, the player collects coins by mining ores. The player, a chicken farmer, may use
 * those coins to plant cabbages on tilled dirt.
 *
 * <p>This class managers the world instance, player manager, and the inventory instance. The
 * inventory and resource overlays should also be managed by this class.
 *
 * <p>In stage 0, this class will store the Brutus character to wander around.
 *
 * @stage1part This class manages the player manager.
 * @stage2part This class manages the world instance.
 */
public class JavaBeanFarm implements Game {

    // Constants for better maintainability
    private static final int DEFAULT_INVENTORY_SIZE = 5;
    private static final int TEMP_INVENTORY_SIZE = 5;
    private static final int TEMP_INVENTORY_COINS = 100;
    private static final int TEMP_INVENTORY_FOOD = 100;

    private final PlayerManager playerManager;
    private final NpcManager npcs;
    private final EnemyManager enemies;
    private final BeanWorld world;
    private final Inventory inventory;
    private final List<Overlay> overlays = new ArrayList<>();

    /**
     * Reads all content from a Reader into a String.
     * 
     * @param reader The reader to read from
     * @return String containing all content from the reader
     * @throws IOException If reading fails
     */
    private String readAllReader(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        StringJoiner sb = new StringJoiner(System.lineSeparator());
        String line;
        while ((line = br.readLine()) != null) {
            sb.add(line);
        }
        return sb.toString();
    }

    /**
     * Constructs a new JavaBean Farm game using the given dimensions, mapPath and detailPath
     *
     * @param dimensions The dimensions we want for this game.
     * @param mapReader A reader the contains a description of the world map.
     * @param detailReader A reader the contains the overlay details for the game,
     *                     e.g. spawner locations.
     * @throws IOException If the game is unable to find or open the default world map file.
     * @throws WorldLoadException If the default world map file cannot be parsed successfully.
     */
    public JavaBeanFarm(Dimensions dimensions, Reader mapReader, Reader detailReader)
            throws IOException, WorldLoadException {

        final String detailsContent = readAllReader(detailReader);
        final PlayerDetails playerDetails = OverlayBuilder.getPlayerDetailsFromFile(detailsContent);
        
        // Initialize core game components
        this.playerManager = initializePlayerManager(playerDetails);
        this.npcs = new NpcManager();
        this.enemies = initializeEnemyManager(dimensions, detailsContent);
        this.world = initializeWorld(dimensions, mapReader, detailsContent);
        this.inventory = initializeInventory(playerDetails);
        initializeOverlays(dimensions);
    }

    /**
     * Alternative constructor that takes file paths instead of readers.
     * 
     * @param dimensions The dimensions we want for this game
     * @param mapFile Path to the map file
     * @param detailsFile Path to the details file
     * @throws IOException If the files cannot be read
     * @throws WorldLoadException If the world cannot be loaded
     */
    public JavaBeanFarm(Dimensions dimensions, String mapFile, String detailsFile) 
            throws IOException, WorldLoadException {
        this(dimensions, new FileReader(mapFile), new FileReader(detailsFile));
    }

    /**
     * Initializes the player manager with the given player details.
     * 
     * @param playerDetails Details containing player's starting position
     * @return Initialized PlayerManager
     */
    private PlayerManager initializePlayerManager(PlayerDetails playerDetails) {
        return new PlayerManager(playerDetails.getX(), playerDetails.getY());
    }

    /**
     * Initializes the enemy manager and adds all spawners from the details.
     * 
     * @param dimensions Game dimensions
     * @param detailsContent String containing spawner configuration
     * @return Initialized EnemyManager with all spawners added
     * @throws IOException If spawner details cannot be parsed
     */
    private EnemyManager initializeEnemyManager(Dimensions dimensions, String detailsContent) 
            throws IOException {
        EnemyManager enemyManager = new EnemyManager(dimensions);
        
        addMagpieSpawners(enemyManager, detailsContent);
        addEagleSpawners(enemyManager, detailsContent);
        addPigeonSpawners(enemyManager, detailsContent);
        
        return enemyManager;
    }

    /**
     * Adds magpie spawners to the enemy manager.
     * 
     * @param enemyManager The enemy manager to add spawners to
     * @param detailsContent String containing spawner configuration
     * @throws IOException If spawner details cannot be parsed
     */
    private void addMagpieSpawners(EnemyManager enemyManager, String detailsContent) 
            throws IOException {
        final List<SpawnerDetails> magpieSpawnPoints =
                OverlayBuilder.getMagpieSpawnDetailsFromString(detailsContent);
        for (SpawnerDetails spawnerDetails : magpieSpawnPoints) {
            enemyManager.add(new MagpieSpawner(
                    spawnerDetails.getX(),
                    spawnerDetails.getY(),
                    spawnerDetails.getDuration()));
        }
    }

    /**
     * Adds eagle spawners to the enemy manager.
     * 
     * @param enemyManager The enemy manager to add spawners to
     * @param detailsContent String containing spawner configuration
     * @throws IOException If spawner details cannot be parsed
     */
    private void addEagleSpawners(EnemyManager enemyManager, String detailsContent) 
            throws IOException {
        final List<SpawnerDetails> eagleSpawnPoints =
                OverlayBuilder.getEagleSpawnDetailsFromString(detailsContent);
        for (SpawnerDetails spawnerDetails : eagleSpawnPoints) {
            enemyManager.add(new EagleSpawner(
                    spawnerDetails.getX(),
                    spawnerDetails.getY(),
                    spawnerDetails.getDuration()));
        }
    }

    /**
     * Adds pigeon spawners to the enemy manager.
     * 
     * @param enemyManager The enemy manager to add spawners to
     * @param detailsContent String containing spawner configuration
     * @throws IOException If spawner details cannot be parsed
     */
    private void addPigeonSpawners(EnemyManager enemyManager, String detailsContent) 
            throws IOException {
        final List<SpawnerDetails> pigeonSpawnPoints =
                OverlayBuilder.getPigeonSpawnDetailsFromString(detailsContent);
        for (SpawnerDetails spawnerDetails : pigeonSpawnPoints) {
            enemyManager.add(new PigeonSpawner(
                    spawnerDetails.getX(),
                    spawnerDetails.getY(),
                    spawnerDetails.getDuration()));
        }
    }

    /**
     * Initializes the game world from map data and places initial cabbages.
     * 
     * @param dimensions Game dimensions
     * @param mapReader Reader containing map data
     * @param detailsContent String containing cabbage placement details
     * @return Initialized BeanWorld
     * @throws IOException If world data cannot be read
     * @throws WorldLoadException If world cannot be constructed
     */
    private BeanWorld initializeWorld(Dimensions dimensions, Reader mapReader, 
                                       String detailsContent) 
            throws IOException, WorldLoadException {
        String worldContent = readAllReader(mapReader);
        BeanWorld world = WorldBuilder.fromTiles(WorldBuilder.fromString(dimensions, worldContent));
        
        placeCabbages(world, dimensions, detailsContent);
        
        return world;
    }

    /**
     * Places initial cabbages in the world based on details configuration.
     * 
     * @param world The world to place cabbages in
     * @param dimensions Game dimensions for tile position calculations
     * @param detailsContent String containing cabbage placement details
     * @throws IOException If cabbage details cannot be parsed
     */
    private void placeCabbages(BeanWorld world, Dimensions dimensions, String detailsContent) 
            throws IOException {
        final List<CabbageDetails> cabbageSpawnPoints =
                OverlayBuilder.getCabbageSpawnDetailsFromString(detailsContent);
        for (CabbageDetails cabbageDetails : cabbageSpawnPoints) {
            final int positionX = cabbageDetails.getX();
            final int positionY = cabbageDetails.getY();
            final List<Tile> tiles = world.tilesAtPosition(positionX, positionY, dimensions);
            for (Tile tile : tiles) {
                if (tile instanceof Dirt dirt) {
                    TinyInventory tempInventory = new TinyInventory(
                        TEMP_INVENTORY_SIZE, TEMP_INVENTORY_COINS, TEMP_INVENTORY_FOOD);
                    dirt.till();
                    dirt.plant(tempInventory);
                }
            }
        }
    }

    /**
     * Initializes the player's inventory with starting resources and default tools.
     * 
     * @param playerDetails Details containing starting coins and food
     * @return Initialized inventory
     */
    private TinyInventory initializeInventory(PlayerDetails playerDetails) {
        TinyInventory inventory = new TinyInventory(
                DEFAULT_INVENTORY_SIZE,
                playerDetails.getStartingCoins(),
                playerDetails.getStartingFood());

        setDefaultInventoryItems(inventory);
        return inventory;
    }

    /**
     * Sets the default tools in the player's inventory.
     * 
     * @param inventory The inventory to populate with default items
     */
    private void setDefaultInventoryItems(TinyInventory inventory) {
        inventory.setItem(0, new Bucket());
        inventory.setItem(1, new Hoe());
        inventory.setItem(2, new Jackhammer());
        inventory.setItem(3, new HiveHammer());
        inventory.setItem(4, new Pole());
    }

    /**
     * Initializes the UI overlays for inventory and resources.
     * 
     * @param dimensions Game dimensions for overlay positioning
     */
    private void initializeOverlays(Dimensions dimensions) {
        this.overlays.add(new InventoryOverlay(dimensions, DEFAULT_INVENTORY_SIZE));
        this.overlays.add(new ResourceOverlay(dimensions));
    }

    /**
     * Ticks the internal game state forward by one frame.
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *     dimension. Useful for processing keyboard presses or mouse movement.
     * @stage1part The player manager should be progressed via {@link
     *     PlayerManager#tick(EngineState, GameState)}.
     * @stage2part The world should be progressed via {@link BeanWorld#tick(EngineState,
     *     GameState)}.
     */
    public void tick(EngineState state) {
        GameState game = new JavaBeanGameState(
                world, playerManager.getPlayer(), inventory, this.npcs, this.enemies);
        
        // Update all game components
        this.playerManager.tick(state, game);
        this.npcs.tick(state, game);
        this.enemies.tick(state, game);
        this.world.tick(state, game);

        // Update overlays
        for (Overlay overlay : overlays) {
            overlay.tick(state, game);
        }

        // Handle interactions
        this.npcs.interact(state, game);
        this.enemies.interact(state, game);

        // Clean up removed entities
        this.npcs.cleanup();
        this.enemies.cleanup();
    }

    /**
     * A collection of items to render, every component of the game to be rendered should be
     * returned.
     *
     * @return The list of renderables required to draw the whole game.
     * @stage2part Any renderables of the world (i.e. {@link BeanWorld#render()}) must be rendered
     *     behind everything else, i.e., first in the returned list.
     * @stage1part Any renderables of the player (i.e. {@link PlayerManager#render()}) must be
     *     rendered after the world but before overlays.
     *     <p>Overlays, i.e., {@link ResourceOverlay} and {@link InventoryOverlay} must be rendered
     *     last in any order.
     */
    @Override
    public List<Renderable> render() {
        List<Renderable> renderables = new ArrayList<>();

        // Render in correct order: world, NPCs, enemies, player, overlays
        renderables.addAll(this.world.render());
        renderables.addAll(this.npcs.render());
        renderables.addAll(this.enemies.render());
        renderables.addAll(this.playerManager.render());

        for (Overlay overlay : overlays) {
            renderables.addAll(overlay.render());
        }

        return renderables;
    }
}