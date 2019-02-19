package de.paul2708.claim.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * This listener is called, if a tree grows.
 *
 * @author Paul2708
 */
public class StructureGrowListener implements Listener {

    /**
     * Cancel the event, if the tree grows into another chunk
     *
     * @param event structure grow event
     */
    @EventHandler
    public void onGrow(StructureGrowEvent event) {
        // TODO: Cancel growing into claimed chunks
    }
}
