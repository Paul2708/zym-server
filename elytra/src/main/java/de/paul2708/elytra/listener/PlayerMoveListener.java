package de.paul2708.elytra.listener;

import de.paul2708.elytra.ElytraPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * This listener is called, if a player moves.
 *
 * @author Paul2708
 */
public class PlayerMoveListener implements Listener {

    private static final Location LOWER_SPAWN = new Location(Bukkit.getWorld("NewWorld"), 123.5, 69, 78.5);

    private static final Location UPPER_SPAWN = new Location(Bukkit.getWorld("NewWorld"), 126.5, 232, 75.5);

    /**
     * Teleport the player to the upper spawn location.
     *
     * @param event player move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        if (to.getWorld().equals(LOWER_SPAWN.getWorld())
                && Math.abs(to.getX() - LOWER_SPAWN.getX()) <= 0.2
                && Math.abs(to.getY() - LOWER_SPAWN.getY()) <= 0.5
                && Math.abs(to.getZ() - LOWER_SPAWN.getZ()) <= 0.2) {
            player.teleport(PlayerMoveListener.UPPER_SPAWN);

            if (player.getInventory().getChestplate() == null
                    || player.getInventory().getChestplate().getType() != Material.ELYTRA) {
                int freeSlot = player.getInventory().firstEmpty();
                if (freeSlot != -1) {
                    player.getInventory().setItem(freeSlot, player.getInventory().getChestplate());
                }

                player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                player.updateInventory();

                player.setMetadata("elytra", new FixedMetadataValue(ElytraPlugin.getInstance(), true));
            }
        }
    }
}
