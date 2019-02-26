package de.paul2708.claim.scoreboard;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * This singleton class manages the used scoreboards.
 *
 * @author Paul2708
 */
public final class ScoreboardManager {

    private static ScoreboardManager instance;

    private Scoreboard scoreboard;
    private Objective objective;

    private Team adminTeam;

    /**
     * Create the scoreboard.
     */
    public void createScoreboard() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Clear objective and teams
        this.objective = scoreboard.getObjective("chunks");
        if (objective != null) {
            objective.unregister();
        }

        this.adminTeam = scoreboard.getTeam("0-admin");
        if (adminTeam != null) {
            adminTeam.unregister();
        }

        // Create object and teams
        this.objective = scoreboard.registerNewObjective("chunks", "dummy", "Chunks", RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        this.adminTeam = scoreboard.registerNewTeam("0-admin");
        adminTeam.setColor(ChatColor.DARK_RED);
    }

    /**
     * Update the scoreboard for a player. It will be set for everyone.
     *
     * @param player player
     */
    public void update(Player player) {
        objective.getScore(player.getName()).setScore(ClaimInformation.get(player.getUniqueId()).getChunks().size());

        if (player.isOp()) {
            if (adminTeam.hasEntry(player.getName())) {
                adminTeam.removeEntry(player.getName());
            }

            adminTeam.addEntry(player.getName());
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(scoreboard));
    }

    /**
     * Get the singleton instance.
     *
     * @return instance
     */
    public static ScoreboardManager getInstance() {
        if (ScoreboardManager.instance == null) {
            ScoreboardManager.instance = new ScoreboardManager();
        }

        return ScoreboardManager.instance;
    }
}
