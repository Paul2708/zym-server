package de.paul2708.claim;

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

    }

    /**
     * Called, if the plugin was disabled.
     */
    @Override
    public void onDisable() {

    }

    /**
     * Get the singleton plugin instance.
     *
     * @return plugin instance
     */
    public static ClaimPlugin getInstance() {
        return ClaimPlugin.instance;
    }
}
