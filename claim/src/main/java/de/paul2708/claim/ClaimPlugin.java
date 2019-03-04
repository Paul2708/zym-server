package de.paul2708.claim;

import de.paul2708.claim.command.ChunkCommand;
import de.paul2708.claim.command.LiveCommand;
import de.paul2708.claim.command.TeleportHelpCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.impl.JsonDatabase;
import de.paul2708.claim.listener.block.*;
import de.paul2708.claim.listener.block.hanging.HangingBreakListener;
import de.paul2708.claim.listener.block.hanging.HangingPlaceListener;
import de.paul2708.claim.listener.entity.EntityBlockFormListener;
import de.paul2708.claim.listener.entity.EntityDamageByEntityListener;
import de.paul2708.claim.listener.entity.EntityExplodeListener;
import de.paul2708.claim.listener.entity.vehicle.VehicleDestroyListener;
import de.paul2708.claim.listener.inventory.InventoryClickListener;
import de.paul2708.claim.listener.item.CraftItemListener;
import de.paul2708.claim.listener.player.*;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This {@link JavaPlugin} represents the main plugin.
 *
 * @author Paul2708
 */
public class ClaimPlugin extends JavaPlugin {

    /**
     * Name of the main world.
     */
    public static final String MAIN_WORLD = "NewWorld";

    /**
     * Standard message prefix.
     */
    public static final String PREFIX = "§8[§3Claim§8] §7";

    private static ClaimPlugin instance;

    private Database database;

    /**
     * Called, if the plugin is loaded.
     */
    @Override
    public void onLoad() {
        ClaimPlugin.instance = this;
    }

    /**
     * Called, if the plugin was enabled.
     */
    @Override
    public void onEnable() {
        // Setup the database
        this.database = new JsonDatabase();

        try {
            this.database.connect();
            this.database.setUp();

            this.database.resolveClaimInformation();

            for (Player player : Bukkit.getOnlinePlayers()) {
                this.database.create(player.getUniqueId());
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Register listener
        registerListener(new PlayerJoinListener());

        registerListener(new BlockBreakListener(), new BlockPlaceListener(), new BlockDamageListener(),
                new StructureGrowListener(), new BlockFromToListener(), new BlockPistonListener(),
                new BlockExplodeListener(), new BlockBurnListener(), new BlockIgniteListener());

        registerListener(new HangingPlaceListener(), new HangingBreakListener());

        registerListener(new EntityDamageByEntityListener(), new EntityExplodeListener(), new EntityBlockFormListener());

        registerListener(new VehicleDestroyListener());

        registerListener(new InventoryClickListener());

        registerListener(new CraftItemListener());

        registerListener(new PlayerInteractListener(), new PlayerArmorStandManipulateListener(),
                new PlayerDropItemListener(), new PlayerInteractAtEntityListener(), new PlayerMoveListener(),
                new PlayerDeathListener(), new PlayerBucketEmptyListener(), new PlayerBucketFillListener(),
                new PlayerLeashEntityListener(), new PlayerQuitListener());

        // Create scoreboard
        ScoreboardManager.getInstance().createScoreboard();

        // Register command
        getCommand("chunk").setExecutor(new ChunkCommand());
        getCommand("live").setExecutor(new LiveCommand());
        getCommand("tphelp").setExecutor(new TeleportHelpCommand());
    }

    /**
     * Called, if the plugin was disabled.
     */
    @Override
    public void onDisable() {
        try {
            this.database.disconnect();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register some listener.
     *
     * @param listener listener
     */
    private void registerListener(Listener... listener) {
        for (Listener single : listener) {
            Bukkit.getPluginManager().registerEvents(single, this);
        }
    }

    /**
     * Get the singleton plugin instance.
     *
     * @return plugin instance
     */
    public static ClaimPlugin getInstance() {
        return ClaimPlugin.instance;
    }

    /**
     * Get the database.
     *
     * @return database
     */
    public Database getDatabase() {
        return database;
    }
}
