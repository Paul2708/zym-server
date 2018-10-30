package de.paul2708.claim.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

/**
 * This listener is called, if a block explodes.
 *
 * @author Paul2708
 */
public class BlockExplodeListener implements Listener {

    /**
     * Cancel the event.
     *
     * @param event block explode event
     */
    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }
}
