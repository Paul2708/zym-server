package de.paul2708.restart;

import de.paul2708.restart.timer.RestartTimer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This {@link JavaPlugin} represents the main plugin.
 *
 * @author Paul2708
 */
public class RestartPlugin extends JavaPlugin {

    private static RestartPlugin instance;

    private RestartTimer timer;

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
        this.timer = new RestartTimer();

        this.timer.start();
    }

    /**
     * Called, if the plugin was disabled.
     */
    @Override
    public void onDisable() {
        this.timer.stop();
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
