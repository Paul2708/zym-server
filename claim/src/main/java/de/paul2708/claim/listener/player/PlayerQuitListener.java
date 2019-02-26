package de.paul2708.claim.listener.player;

import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This listener is called, if a player leaves the server.
 *
 * @author Paul2708
 */
public class PlayerQuitListener implements Listener {

    /**
     * Update the tablist.
     *
     * @param event player join event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ScoreboardManager.getInstance().updateColors(event.getPlayer(), false);

        ScoreboardManager.getInstance().updateHeaderAndFooter(Bukkit.getOnlinePlayers().size() - 1);
    }
}
