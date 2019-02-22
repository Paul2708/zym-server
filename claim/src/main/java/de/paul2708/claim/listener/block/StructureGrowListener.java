package de.paul2708.claim.listener.block;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
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
        BlockState origin = null;

        for (BlockState block : event.getBlocks()) {
            switch (block.getType()) {
                case DIRT:
                case ACACIA_LOG:
                case BIRCH_LOG:
                case DARK_OAK_LOG:
                case JUNGLE_LOG:
                case OAK_LOG:
                case SPRUCE_LOG:
                case STRIPPED_ACACIA_LOG:
                case STRIPPED_BIRCH_LOG:
                case STRIPPED_DARK_OAK_LOG:
                case STRIPPED_JUNGLE_LOG:
                case STRIPPED_OAK_LOG:
                case STRIPPED_SPRUCE_LOG:
                case MUSHROOM_STEM:
                    origin = block;
                    break;
                default:
                    origin = null;
                    break;
            }

            if (origin != null) {
                break;
            }
        }

        if (origin != null) {
            ClaimInformation originInformation = ClaimInformation.get(origin.getChunk());

            for (BlockState block : event.getBlocks()) {
                if (ClaimInformation.isClaimed(block.getChunk())) {
                    ClaimInformation information = ClaimInformation.get(block.getChunk());

                    if (originInformation == null || !originInformation.getUuid().equals(information.getUuid())) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
