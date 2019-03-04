package de.paul2708.claim.listener.entity;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

/**
 * This listener is called, if an entity changes blocks.
 *
 * @author Paul2708
 */
public class EntityBlockFormListener implements Listener {

    /**
     * Cancel the event, if it's caused by frost walker on a claimed chunk.
     *
     * @param event entity block from event
     */
    @EventHandler
    public void onForm(EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getNewState() != null && event.getNewState().getType() == Material.FROSTED_ICE) {
                if (ClaimInformation.isClaimedByOthers((Player) event.getEntity(), event.getNewState().getChunk())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
