package de.paul2708.claim.listener.block;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * This listener is called, if a player breaks a block.
 *
 * @author Paul2708
 */
public class BlockBreakListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event block break event
     */
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (ClaimInformation.isClaimedByOthers(player, event.getBlock().getChunk())) {
            event.setCancelled(true);

            player.sendMessage(ClaimPlugin.PREFIX + "§CDu kannst nur auf deinen eigenen Chunks abbauen.");
        }
    }
}
