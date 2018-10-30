package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * This listener is called, if a player interacts.
 *
 * @author Paul2708
 */
public class PlayerInteractListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event player interact event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

            if (ClaimInformation.isClaimedByOthers(player, chunk)) {
                event.setCancelled(true);
            }
        }
    }
}
