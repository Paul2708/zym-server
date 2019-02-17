package de.paul2708.claim.listener.entity;

import de.paul2708.claim.ClaimPlugin;
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
                if (player.hasMetadata("elytra")) {
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    player.updateInventory();

                    player.removeMetadata("elytra", ClaimPlugin.getInstance());
                }
            }
        }
    }
}
