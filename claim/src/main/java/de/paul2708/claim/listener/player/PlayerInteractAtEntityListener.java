package de.paul2708.claim.listener.player;

import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * This listener is called, if a player interacts with an entity.
 *
 * @author Paul2708
 */
public class PlayerInteractAtEntityListener implements Listener {

    /**
     * Cancel the event, if the item is the claimer or its a frame.
     *
     * @param event player interact entity event
     */
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (ItemManager.getInstance().isClaimer(player.getInventory().getItemInMainHand())) {
            event.setCancelled(true);

            player.updateInventory();
        } else {
            if (Utility.hasBypass(player)) {
                return;
            }

            if (ClaimInformation.isClaimedByOthers(player, event.getRightClicked().getLocation().getChunk())) {
                event.setCancelled(true);

                player.updateInventory();
            }
        }
    }
}
