package de.paul2708.claim.listener.block.hanging;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

/**
 * This listener is called, if an hanging is placed.
 *
 * @author Paul2708
 */
public class HangingPlaceListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event hanging place event
     */
    @EventHandler
    public void onPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();

        if (Utility.hasBypass(player)) {
            return;
        }

        if (ClaimInformation.isClaimedByOthers(player, event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }
}
