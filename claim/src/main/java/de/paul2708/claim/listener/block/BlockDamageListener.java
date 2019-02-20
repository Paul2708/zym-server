package de.paul2708.claim.listener.block;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * This listener is called, if a player tries to damage a block.
 *
 * @author Paul2708
 */
public class BlockDamageListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event block damage event
     */
    @EventHandler
    public void onDamage(BlockDamageEvent event) {
        if (Utility.hasBypass(event.getPlayer())) {
            return;
        }

        if (ClaimInformation.isClaimedByOthers(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }
}
