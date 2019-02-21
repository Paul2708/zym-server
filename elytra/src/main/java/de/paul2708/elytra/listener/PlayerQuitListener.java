package de.paul2708.elytra.listener;

import de.paul2708.elytra.item.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player quits the server.
 *
 * @author Paul2708
 */
public class PlayerQuitListener implements Listener {

    private ItemManager itemManager;

    /**
     * Create a new player quit listener.
     *
     * @param itemManager item manager
     */
    public PlayerQuitListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Remove the elytra.
     *
     * @param event player quit event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (itemManager.isElytra(player.getInventory().getChestplate())) {
            player.getInventory().setChestplate(itemManager.getEmptyItem());
        } else {
            ItemStack item = null;

            for (ItemStack drop : event.getPlayer().getInventory()) {
                if (itemManager.isElytra(drop)) {
                    item = drop;
                    break;
                }
            }

            if (item != null) {
                player.getInventory().remove(item);
            }
        }
    }
}
