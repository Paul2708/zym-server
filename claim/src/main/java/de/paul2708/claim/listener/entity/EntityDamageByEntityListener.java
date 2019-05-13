package de.paul2708.claim.listener.entity;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ProfileManager;
import org.bukkit.entity.*;
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
        Player player = getDamager(event.getDamager());

        if (player != null) {
            if (!ProfileManager.getInstance().hasAccess(player, event.getEntity().getLocation().getChunk())) {
                if (event.getEntity() instanceof Monster || event.getEntity() instanceof Phantom) {
                    return;
                }

                event.setCancelled(true);
                event.getEntity().setFireTicks(0);

                player.sendMessage(ClaimPlugin.PREFIX + "§CDu kannst hier keine Mobs töten.");
            }
        }
    }

    /**
     * Get the damager by entity.
     *
     * @param entity entity damager
     * @return player damager or <code>null</code> if it isn't a player
     */
    private Player getDamager(Entity entity) {
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
