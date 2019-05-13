package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
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

        player.sendTitle("§cCommunity Attack", "", 20, 60, 20);

        ScoreboardManager.getInstance().updateChunkCounter(player);
        ScoreboardManager.getInstance().updateColors(player, false);

        ScoreboardManager.getInstance().updateHeaderAndFooter(Bukkit.getOnlinePlayers().size());

        // Profile creation
        if (ProfileManager.getInstance().doesProfileExist(player.getUniqueId())) {
            return;
        }

        ClaimPlugin.getInstance().getDatabase().create(player.getUniqueId(), new DatabaseResult<Integer>() {

            @Override
            public void success(Integer result) {
                ClaimProfile profile = new ClaimProfile(player.getUniqueId(), 0);
                profile.setId(result);

                ProfileManager.getInstance().addProfile(profile);
            }

            @Override
            public void exception(DatabaseException exception) {
                player.kickPlayer(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                exception.printStackTrace();
            }
        });
    }
}
