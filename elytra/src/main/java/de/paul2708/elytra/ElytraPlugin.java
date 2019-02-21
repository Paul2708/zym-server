package de.paul2708.elytra;

import de.paul2708.elytra.item.ItemManager;
import de.paul2708.elytra.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This {@link JavaPlugin} represents the main plugin.
 *
 * @author Paul2708
 */
public class ElytraPlugin extends JavaPlugin {

    private static ElytraPlugin instance;

    /**
     * Called, if the plugin is loaded.
     */
    @Override
    public void onLoad() {
        ElytraPlugin.instance = this;
    }

    /**
     * Called, if the plugin was enabled.
     */
    @Override
    public void onEnable() {
        ItemManager itemManager = new ItemManager();
        itemManager.createElytra();

        // Register listener
        registerListener(new PlayerMoveListener(itemManager), new EntityToggleGlideListener(itemManager),
                new PlayerDeathListener(itemManager), new PlayerQuitListener(itemManager),
                new InventoryClickListener(itemManager));
    }

    /**
     * Called, if the plugin was disabled.
     */
    @Override
    public void onDisable() {

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
    public static ElytraPlugin getInstance() {
        return ElytraPlugin.instance;
    }
}
