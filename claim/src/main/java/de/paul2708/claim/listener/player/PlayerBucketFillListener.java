package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * This listener is called, if a player fills a bucket.
 *
 * @author Paul2708
 */
public class PlayerBucketFillListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event player bucket fill event
     */
    @EventHandler
    public void onEmpty(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();

        if (!ProfileManager.getInstance().hasAccess(player, event.getBlockClicked().getChunk())) {
            event.setCancelled(true);
        }
    }
}
