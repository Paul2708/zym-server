package de.paul2708.claim.listener.entity;

import de.paul2708.claim.ClaimPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if an entity toggles gliding.
 *
 * @author Paul2708
 */
public class EntityToggleGlideListener implements Listener {

    private static final Location UPPER_CORNER = new Location(Bukkit.getWorld("NewWorld"), 108, 220, 95);

    private static final Location LOWER_CORNER = new Location(Bukkit.getWorld("NewWorld"), 141, 248, 61);

    /**
     * Remove the elytra.
     *
     * @param event entity toggle glide event
     */
    @EventHandler
    public void onGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!event.isGliding()) {
                if (isInArea(player.getLocation())) {
                    return;
                }

                if (player.hasMetadata("elytra")) {
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    player.updateInventory();

                    player.removeMetadata("elytra", ClaimPlugin.getInstance());
                }
            }
        }
    }

    /**
     * Check if the location is in the area.
     *
     * @param location location
     * @return true if the location is in the area, otherwise false
     */
    private boolean isInArea(Location location) {
        if (location.getWorld().getName().equals(UPPER_CORNER.getWorld().getName())) {
            if (location.getX() >= UPPER_CORNER.getX() && location.getX() <= LOWER_CORNER.getX()) {
                if (location.getY() >= UPPER_CORNER.getY() && location.getY() <= LOWER_CORNER.getY()) {
                    if (location.getZ() >= LOWER_CORNER.getZ() && location.getZ() <= UPPER_CORNER.getZ()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
