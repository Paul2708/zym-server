package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

/**
 * This listener is called, if a player tries to manipulate an armor stand.
 *
 * @author Paul2708
 */
public class PlayerArmorStandManipulateListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event player armor stand manipulate event
     */
    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();

        if (!ProfileManager.getInstance().hasAccess(player, event.getRightClicked().getLocation().getChunk())) {
            event.setCancelled(true);
        }
    }
}
