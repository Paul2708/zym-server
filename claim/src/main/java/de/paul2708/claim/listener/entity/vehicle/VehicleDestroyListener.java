package de.paul2708.claim.listener.entity.vehicle;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

/**
 * This listener is called, if a vehicle gets destroyed.
 *
 * @author Paul2708
 */
public class VehicleDestroyListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.
     *
     * @param event vehicle destroy event
     */
    @EventHandler
    public void onDestroy(VehicleDestroyEvent event) {
        Player attacker = getAttacker(event.getAttacker());

        if (attacker != null) {
            if (Utility.hasBypass(attacker)) {
                return;
            }

            if (ClaimInformation.isClaimedByOthers(attacker, event.getVehicle().getLocation().getChunk())) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Get the attacker by entity.
     *
     * @param entity entity damager
     * @return player damager or <code>null</code> if it isn't a player
     */
    private Player getAttacker(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        } else if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;

            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }

        return null;
    }
}
