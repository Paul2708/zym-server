package de.paul2708.elytra.listener;

import de.paul2708.elytra.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

/**
 * This listener is called, if a player picks up an item.
 *
 * @author Paul2708
 */
public class ItemSpawnListener implements Listener {

    private ItemManager itemManager;

    /**
     * Create a new item spawn listener.
     *
     * @param itemManager item manager
     */
    public ItemSpawnListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Remove the elytra.
     *
     * @param event item spawn event
     */
    @EventHandler
    public void onSpawn(ItemSpawnEvent event) {
        if (event.getEntity() != null) {
            if (itemManager.isElytra(event.getEntity().getItemStack())) {
                event.getEntity().remove();

                event.setCancelled(true);
            }
        }
    }
}
