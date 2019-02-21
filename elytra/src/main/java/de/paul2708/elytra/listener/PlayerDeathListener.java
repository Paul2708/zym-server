package de.paul2708.elytra.listener;

import de.paul2708.elytra.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player dies.
 *
 * @author Paul2708
 */
public class PlayerDeathListener implements Listener {

    private ItemManager itemManager;

    /**
     * Create a new player death listener.
     *
     * @param itemManager item manager
     */
    public PlayerDeathListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Remove the elytra from the drops.
     *
     * @param event player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ItemStack item = null;
        for (ItemStack drop : event.getDrops()) {
            if (itemManager.isElytra(drop)) {
                item = drop;
                break;
            }
        }

        if (item != null) {
            event.getDrops().remove(item);
        }
    }
}
