package de.paul2708.claim.listener.player;

import de.paul2708.claim.util.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
     * Remove the claimer from the dropped items.
     *
     * @param event player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        List<ItemStack> list = new LinkedList<>();
        for (ItemStack drop : event.getDrops()) {
            if (Utility.isClaimer(drop)) {
                list.add(drop);
            }
        }

        for (ItemStack itemStack : list) {
            event.getDrops().remove(itemStack);
        }
    }
}
