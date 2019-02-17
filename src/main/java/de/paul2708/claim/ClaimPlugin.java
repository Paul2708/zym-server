package de.paul2708.claim;

import de.paul2708.claim.command.ChunkCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.impl.JsonDatabase;
import de.paul2708.claim.listener.PlayerJoinListener;
import de.paul2708.claim.listener.block.*;
import de.paul2708.claim.listener.entity.EntityDamageByEntityListener;
import de.paul2708.claim.listener.entity.EntityExplodeListener;
import de.paul2708.claim.listener.entity.EntityToggleGlideListener;
import de.paul2708.claim.listener.inventory.InventoryClickListener;
import de.paul2708.claim.listener.item.CraftItemListener;
import de.paul2708.claim.listener.player.*;
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
        registerEvents(new PlayerJoinListener());

        registerEvents(new BlockBreakListener(), new BlockPlaceListener(), new BlockDamageListener(),
                new StructureGrowListener(), new BlockFromToListener(), new BlockPistonListener(),
                new BlockExplodeListener());

        registerEvents(new EntityDamageByEntityListener(), new EntityExplodeListener(),
                new EntityToggleGlideListener());

        registerEvents(new InventoryClickListener());

        registerEvents(new CraftItemListener());

        registerEvents(new PlayerInteractListener(), new PlayerArmorStandManipulateListener(),
                new PlayerDropItemListener(), new PlayerInteractAtEntityListener(), new PlayerMoveListener());

        // Register command
        getCommand("chunk").setExecutor(new ChunkCommand());
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
    private void registerEvents(Listener... listener) {
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
