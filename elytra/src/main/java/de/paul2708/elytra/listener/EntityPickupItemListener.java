package de.paul2708.elytra.listener;

import de.paul2708.elytra.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * This listener is called, if a player picks up an item.
 *
 * @author Paul2708
 */
public class EntityPickupItemListener implements Listener {

    private ItemManager itemManager;

    /**
     * Create a new player pickup item listener.
     *
     * @param itemManager item manager
     */
    public EntityPickupItemListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Remove the elytra.
     *
     * @param event entity pickup item event
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getItem() != null) {
            if (itemManager.isElytra(event.getItem().getItemStack())) {
                event.getItem().remove();

                event.setCancelled(true);
            }
        }
    }
}
