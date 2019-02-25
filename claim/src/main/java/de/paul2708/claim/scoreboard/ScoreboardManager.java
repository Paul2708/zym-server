package de.paul2708.claim.scoreboard;

import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

/**
 * This singleton class manages the used scoreboards.
 *
 * @author Paul2708
 */
public final class ScoreboardManager {

    private static ScoreboardManager instance;

    private Scoreboard scoreboard;
    private Objective objective;

    /**
     * Create the scoreboard.
     */
    public void createScoreboard() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("chunks", "dummy", "", RenderType.INTEGER);

        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    /**
     * Update the score for a player.
     *
     * @param player player
     */
    public void update(Player player) {
        objective.getScore(player.getName()).setScore(ClaimInformation.get(player.getUniqueId()).getChunks().size());

        player.setScoreboard(scoreboard);
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
