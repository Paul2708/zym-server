package de.paul2708.claim.listener.entity.creature;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * This listener is called, if a creature spawns.
 *
 * @author Paul2708
 */
public class CreatureSpawnListener implements Listener {

    /**
     * Cancel the spawning of wither.
     *
     * @param event creature spawn event
     */
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }
    }
}
