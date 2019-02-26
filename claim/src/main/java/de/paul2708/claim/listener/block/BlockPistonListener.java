package de.paul2708.claim.listener.block;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

/**
 * This listener is called, if a piston extends.
 *
 * @author Paul2708
 */
public class BlockPistonListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event block piston extend event
     */
    @EventHandler
    public void onExtend(BlockPistonExtendEvent event) {
        ClaimInformation originInformation = ClaimInformation.get(event.getBlock().getChunk());

        for (Block block : event.getBlocks()) {
            BlockFace direction = event.getDirection();
            Location movedLocation = block.getLocation().clone().add(direction.getModX(), direction.getModY(),
                    direction.getModZ());

            if (ClaimInformation.isClaimed(movedLocation.getChunk())) {
                ClaimInformation information = ClaimInformation.get(movedLocation.getChunk());

                if (originInformation == null || !originInformation.getUuid().equals(information.getUuid())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event block piston retract event
     */
    @EventHandler
    public void onRetract(BlockPistonRetractEvent event) {
        ClaimInformation originInformation = ClaimInformation.get(event.getBlock().getChunk());

        for (Block block : event.getBlocks()) {
            if (ClaimInformation.isClaimed(block.getChunk())) {
                ClaimInformation information = ClaimInformation.get(block.getChunk());

                if (originInformation == null || !originInformation.getUuid().equals(information.getUuid())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
