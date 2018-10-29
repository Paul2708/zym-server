package de.paul2708.claim;

import de.paul2708.claim.command.ClaimCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.impl.JsonDatabase;
import de.paul2708.claim.file.AbstractConfiguration;
import de.paul2708.claim.file.InvalidValueException;
import de.paul2708.claim.file.impl.ClaimConfiguration;
import de.paul2708.claim.listener.PlayerJoinListener;
import de.paul2708.claim.listener.block.*;
import de.paul2708.claim.listener.entity.EntityDamageByEntityListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This {@link JavaPlugin} represents the main plugin.
 *
 * @author Paul2708
 */
public class ClaimPlugin extends JavaPlugin {

    // TODO: Interacting, like potions etc

    /**
     * Standard message prefix.
     */
    public static final String PREFIX = "§8[§3Claim§8] §7";

    private static ClaimPlugin instance;

    private AbstractConfiguration configuration;

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
        // Load the configuration
        this.configuration = new ClaimConfiguration();

        try {
            this.configuration.load();
        } catch (InvalidValueException e) {
            e.printStackTrace();
        }

        // Setup the database
        this.database = new JsonDatabase();

        try {
            this.database.connect();
            this.database.setUp();

            this.database.resolveClaimInformation();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        // Register listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new StructureGrowListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockFromToListener(), this);

        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);

        // Register command
        getCommand("claim").setExecutor(new ClaimCommand());
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
     * Get the singleton plugin instance.
     *
     * @return plugin instance
     */
    public static ClaimPlugin getInstance() {
        return ClaimPlugin.instance;
    }

    /**
     * Get the configuration.
     *
     * @return configuration
     */
    public AbstractConfiguration getConfiguration() {
        return configuration;
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
