package de.paul2708.claim.listener.item;

import de.paul2708.claim.util.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player crafts an item.
 *
 * @author Paul2708
 */
public class CraftItemListener implements Listener {

    /**
     * Cancel the event, if the claimer is involved.
     *
     * @param event craft item event
     */
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        for (ItemStack matrix : event.getInventory().getMatrix()) {
            if (ItemManager.isClaimer(matrix)) {
                event.setCancelled(true);
            }
        }
    }
}
