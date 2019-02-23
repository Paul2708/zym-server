package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * This listener is called, if a player dies.
 *
 * @author Paul2708
 */
public class PlayerDeathListener implements Listener {

    /**
     * Keep the claimer.
     *
     * @param event player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Set<ItemStack> claimer = new HashSet<>();
        for (ItemStack drop : event.getDrops()) {
            if (ItemManager.getInstance().isClaimer(drop)) {
                claimer.add(drop);
            }
        }

        event.getDrops().removeAll(claimer);

        // Give claimer back
        Bukkit.getScheduler().scheduleSyncDelayedTask(ClaimPlugin.getInstance(), () -> {
            for (ItemStack itemStack : claimer) {
                event.getEntity().getInventory().addItem(itemStack);
            }

            event.getEntity().updateInventory();
        }, 10);
    }
}