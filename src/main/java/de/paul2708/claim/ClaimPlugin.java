package de.paul2708.claim;

import de.paul2708.claim.command.ClaimCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.impl.JsonDatabase;
import de.paul2708.claim.file.AbstractConfiguration;
import de.paul2708.claim.file.InvalidValueException;
import de.paul2708.claim.file.impl.ClaimConfiguration;
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
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

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
