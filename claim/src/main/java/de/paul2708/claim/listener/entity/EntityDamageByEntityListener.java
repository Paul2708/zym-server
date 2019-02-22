package de.paul2708.claim.listener.entity;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This listener is called, if a player damages an entity.
 *
 * @author Paul2708
 */
public class EntityDamageByEntityListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event entity damage by entity event
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (Utility.hasBypass(player)) {
                return;
            }

            if (ClaimInformation.isClaimedByOthers(player, event.getEntity().getLocation().getChunk())) {
                if (event.getEntity() instanceof Monster) {
                    return;
                }

                event.setCancelled(true);

                player.sendMessage(ClaimPlugin.PREFIX + "§CDu kannst hier keine Mobs töten.");
            }
        }
    }
}
