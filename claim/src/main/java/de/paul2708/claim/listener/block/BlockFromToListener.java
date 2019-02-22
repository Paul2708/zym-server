package de.paul2708.claim.listener.block;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

/**
 * This listener is called, if water or lava flows.
 *
 * @author Paul2708
 */
public class BlockFromToListener implements Listener {

    /**
     * Cancel the event, if the water flows out the chunk.
     *
     * @param event block from to event
     */
    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        if (ClaimInformation.isClaimed(event.getToBlock().getChunk())) {
            ClaimInformation originInformation = ClaimInformation.get(event.getBlock().getChunk());

            if (originInformation == null) {
                event.setCancelled(true);
            } else if (!originInformation.getUuid().equals(ClaimInformation.get(event.getToBlock().getChunk())
                    .getUuid())) {
                event.setCancelled(true);
            }
        }
    }
}
