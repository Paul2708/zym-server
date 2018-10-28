package de.paul2708.claim.listener.block;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
     * @param event block break event
     */
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Pair<Integer, Integer> chunk = Pair.createBy(event.getBlock().getChunk());

        if (!ClaimInformation.get(player.getUniqueId()).contains(chunk)) {
            event.setCancelled(true);

            player.sendMessage(ClaimPlugin.PREFIX + "§CDu kannst nur auf deinen eigenen Chunks bauen.");
        }
    }
}
