package de.paul2708.claim.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

/**
 * This listener is called, if a block burns.
 *
 * @author Paul2708
 */
public class BlockBurnListener implements Listener {
    /**
     * Cancel the event.
     *
     * @param event block burn event
     */
    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
}
