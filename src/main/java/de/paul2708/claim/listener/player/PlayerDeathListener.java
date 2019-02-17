package de.paul2708.claim.listener.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player dies.
 *
 * @author Paul2708
 */
public class PlayerDeathListener implements Listener {

    /**
     * Remove the claimer from the dropped items.
     *
     * @param event player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasMetadata("elytra")) {
            ItemStack elytra = null;
            for (ItemStack drop : event.getDrops()) {
                if (drop.getType() == Material.ELYTRA) {
                    elytra = drop;
                    break;
                }
            }

            if (elytra != null) {
                event.getDrops().remove(elytra);
            }
        }
    }
}