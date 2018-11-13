package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.util.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * This listener is called, if a player drops an item.
 *
 * @author Paul2708
 */
public class PlayerDropItemListener implements Listener {

    /**
     * Cancel the event, if the item is the claimer.
     *
     * @param event player drop item event
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (ItemManager.isClaimer(event.getItemDrop().getItemStack())) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDu kannst deinen Claimer nicht droppen.");

            event.setCancelled(true);
        }
    }
}
