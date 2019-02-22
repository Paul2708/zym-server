package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.util.Utility;
import org.bukkit.Chunk;
import org.bukkit.Material;
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
     * Cancel the event, if the player doesn't own the chunk.<br>
     * And claim a chunk.
     *
     * @param event player interact event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            if (Utility.hasBypass(player)) {
                return;
            }

            Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

            if (ClaimInformation.isClaimedByOthers(player, chunk)) {
                event.setCancelled(true);
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

            if (ClaimInformation.isClaimedByOthers(player, chunk) && !Utility.hasBypass(player)) {
                if (block != null && block.getType().isInteractable() && block.getType() != Material.ENDER_CHEST) {
                    event.setCancelled(true);
                } else if (event.getItem() != null) {
                    switch (event.getItem().getType()) {
                        case BONE_MEAL:
                            event.setCancelled(true);
                        default:
                            break;
                    }
                }
            }

            // Start claiming
            if (!ItemManager.getInstance().isClaimer(player.getInventory().getItemInMainHand())) {
                return;
            }

            event.setCancelled(true);

            player.performCommand("chunk claim");
        }
    }
}
