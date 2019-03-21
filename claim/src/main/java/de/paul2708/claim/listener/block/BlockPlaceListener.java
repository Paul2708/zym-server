package de.paul2708.claim.listener.block;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This listener is called, if a player places a block.
 *
 * @author Paul2708
 */
public class BlockPlaceListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event block place event
     */
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!ProfileManager.getInstance().hasAccess(player, event.getBlock().getChunk())) {
            event.setCancelled(true);

            player.sendMessage(ClaimPlugin.PREFIX + "§CDu kannst nur auf deinen eigenen Chunks bauen.");
        }
    }
}
