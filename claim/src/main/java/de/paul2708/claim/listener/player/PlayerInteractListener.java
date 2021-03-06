package de.paul2708.claim.listener.player;

import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.model.ProfileManager;
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

    private static final Material[] WHITELIST = new Material[] {
            Material.ENDER_CHEST, Material.BIRCH_BUTTON, Material.BIRCH_PRESSURE_PLATE, Material.ENDER_PEARL,
            Material.ENCHANTING_TABLE,
            // Beds
            Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED, Material.LIGHT_BLUE_BED, Material.YELLOW_BED,
            Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.CYAN_BED,
            Material.PURPLE_BED, Material.BLUE_BED, Material.BROWN_BED, Material.GREEN_BED, Material.RED_BED,
            Material.BLACK_BED
    };

    private static final Material[] BLACKLIST = new Material[] {
            Material.BONE_MEAL, Material.ARMOR_STAND
    };

    /**
     * Cancel the event, if the player doesn't own the chunk.<br>
     * And claim a chunk.
     *
     * @param event player interact event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Start claiming
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ItemManager.getInstance().isClaimer(player.getInventory().getItemInMainHand())) {
                event.setCancelled(true);

                player.performCommand("chunk claim");
                return;
            }
        }

        // Handle blocking
        Block block = event.getClickedBlock();
        Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

        if (event.getAction() == Action.PHYSICAL) {
            if (!ProfileManager.getInstance().hasAccess(player, chunk)) {
                if (block == null || !isWhitelisted(block.getType())) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!ProfileManager.getInstance().hasAccess(player, chunk)) {
                if (block != null && block.getType().isInteractable() && !isWhitelisted(block.getType())) {
                    event.setCancelled(true);
                } else if (event.getItem() != null && isBlacklisted(event.getItem().getType())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Check if a material is blacklisted.
     *
     * @param material material to check
     * @return true if the material is blacklisted, otherwise false
     */
    private boolean isBlacklisted(Material material) {
        for (Material value : PlayerInteractListener.BLACKLIST) {
            if (value == material) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a material is whitelited.
     *
     * @param material material to check
     * @return true if the material is whitelisted, otherwise false
     */
    private boolean isWhitelisted(Material material) {
        for (Material value : PlayerInteractListener.WHITELIST) {
            if (value == material) {
                return true;
            }
        }

        return false;
    }
}
