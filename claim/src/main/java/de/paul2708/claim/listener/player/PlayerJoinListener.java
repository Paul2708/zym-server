package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
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

            player.sendTitle("§cCommunity Attack", "", 20, 60, 20);

            ScoreboardManager.getInstance().updateChunkCounter(player);
            ScoreboardManager.getInstance().updateColors(player, false);

            ScoreboardManager.getInstance().updateHeaderAndFooter(Bukkit.getOnlinePlayers().size());
        } catch (DatabaseException e) {
            e.printStackTrace();

            player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten. Joine erneut.");
        }
    }
}
