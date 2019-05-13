package de.paul2708.claim.listener.block.hanging;

import de.paul2708.claim.model.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

/**
 * This listener is called, if an hanging gets broken.
 *
 * @author Paul2708
 */
public class HangingBreakListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event hanging break by entity event
     */
    @EventHandler
    public void onBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();

            if (!ProfileManager.getInstance().hasAccess(player, event.getEntity().getLocation().getChunk())) {
                event.setCancelled(true);
            }
        }
    }
}
