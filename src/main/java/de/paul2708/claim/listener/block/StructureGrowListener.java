package de.paul2708.claim.listener.block;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * This listener is called, if a tree grows.
 *
 * @author Paul2708
 */
public class StructureGrowListener implements Listener {

    /**
     * Cancel the event, if the tree grows into another chunk
     *
     * @param event structure grow event
     */
    @EventHandler
    public void onGrow(StructureGrowEvent event) {
        Player player = event.getPlayer();

        if (player != null) {
            for (BlockState block : event.getBlocks()) {
                if (!ClaimInformation.owns(player, block.getChunk())) {
                    block.setType(Material.AIR);
                }
            }
        } else {
            for (BlockState block : event.getBlocks()) {
                for (BlockState otherBlock : event.getBlocks()) {
                    if (!ClaimInformation.hasSameOwner(block.getChunk(), otherBlock.getChunk())) {
                        event.setCancelled(true);
                    }
                }
            }
        }

    }
}
