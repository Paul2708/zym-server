package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

/**
 * This listener is called, if a player tries to leash an entity.
 *
 * @author Paul2708
 */
public class PlayerLeashEntityListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event player leash entity event
     */
    @EventHandler
    public void onLeash(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();

        if (Utility.hasBypass(player)) {
            return;
        }

        if (ClaimInformation.isClaimedByOthers(player, event.getEntity().getLocation().getChunk())) {
            event.setCancelled(true);

            player.updateInventory();
        }
    }
}
