package de.paul2708.claim.listener;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This listener is called, if a player joins the server.
 *
 * @author Paul2708
 */
public class PlayerJoinListener implements Listener {

    /**
     * Create a new database entry, if it doesn't exist.
     *
     * @param event player join event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try {
            ClaimPlugin.getInstance().getDatabase().create(player.getUniqueId());
        } catch (DatabaseException e) {
            e.printStackTrace();

            player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten. Joine erneut.");
        }
    }
}
