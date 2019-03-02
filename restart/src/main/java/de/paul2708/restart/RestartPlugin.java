package de.paul2708.restart;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This {@link JavaPlugin} represents the main plugin.
 *
 * @author Paul2708
 */
public class RestartPlugin extends JavaPlugin {

    private static RestartPlugin instance;

    /**
     * Called, if the plugin is loaded.
     */
    @Override
    public void onLoad() {
        RestartPlugin.instance = this;
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
    public static RestartPlugin getInstance() {
        return RestartPlugin.instance;
    }
}
