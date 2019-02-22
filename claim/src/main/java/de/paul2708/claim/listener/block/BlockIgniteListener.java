package de.paul2708.claim.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

/**
 * This listener is called, if a block ignites.
 *
 * @author Paul2708
 */
public class BlockIgniteListener implements Listener {

    /**
     * Cancel the event.
     *
     * @param event block ignite event
     */
    @EventHandler
    public void onSpread(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }
}
