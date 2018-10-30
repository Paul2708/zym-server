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
        if (!ClaimInformation.hasSameOwner(event.getBlock().getChunk(), event.getToBlock().getChunk())) {
            event.setCancelled(true);
        }
    }
}
