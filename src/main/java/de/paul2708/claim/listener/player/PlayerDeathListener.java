package de.paul2708.claim.listener.player;

import de.paul2708.claim.util.ItemManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 * This listener is called, if a player dies.
 *
 * @author Paul2708
 */
public class PlayerDeathListener implements Listener {

    /**
     * Remove the elytra and keep the claimer.
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

        // Keep claimer
        List<ItemStack> list = new LinkedList<>();
        Inventory inventory = event.getEntity().getInventory();
        for (ItemStack itemStack : inventory) {
            if (itemStack != null && !ItemManager.getInstance().isClaimer(itemStack)) {
                list.add(itemStack);
            }
        }

        for (ItemStack itemStack : list) {
            event.getEntity().getLocation().getWorld().dropItemNaturally(event.getEntity().getLocation(), itemStack);
            inventory.remove(itemStack);
        }
        event.getEntity().getInventory().setArmorContents(new ItemStack[4]);

        event.setKeepInventory(true);
    }
}