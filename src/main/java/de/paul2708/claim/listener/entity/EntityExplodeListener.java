package de.paul2708.claim.listener.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * This listener is called, if an entity explodes.
 *
 * @author Paul2708
 */
public class EntityExplodeListener implements Listener {

    /**
     * Cancel the event.
     *
     * @param event block explode event
     */
    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }
}
