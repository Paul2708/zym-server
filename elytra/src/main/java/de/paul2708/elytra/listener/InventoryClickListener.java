package de.paul2708.elytra.listener;

import de.paul2708.elytra.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This listener is called, if a player clicks into an inventory.
 *
 * @author Paul2708
 */
public class InventoryClickListener implements Listener {

    private ItemManager itemManager;

    /**
     * Create a new inventory click listener.
     *
     * @param itemManager item manager
     */
    public InventoryClickListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Cancel the interaction with the elytra.
     *
     * @param event inventory click event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (itemManager.isElytra(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }
}
