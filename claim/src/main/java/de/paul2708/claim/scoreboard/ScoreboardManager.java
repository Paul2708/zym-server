package de.paul2708.claim.scoreboard;

import de.paul2708.claim.model.ClaimInformation;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * This singleton class manages the used scoreboards.
 *
 * @author Paul2708
 */
public final class ScoreboardManager {

    private static final String HEADER = "§6§lCommunity Attack\n§eSubserver\n";

    private static final String FOOTER = "\n§a%count% §7Spieler online\n§8powered by §6Nitrado";

    private static ScoreboardManager instance;

    private Scoreboard scoreboard;
    private Objective objective;

    private Team adminTeam;
    private Team userTeam;

    private Team liveTeam;
    private Team adminLiveTeam;

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

        this.unregisterTeam("0-admin");
        this.unregisterTeam("1-admin_live");
        this.unregisterTeam("2-live");
        this.unregisterTeam("3-user");

        // Create object and teams
        this.objective = scoreboard.registerNewObjective("chunks", "dummy", "Chunks", RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        this.adminTeam = scoreboard.registerNewTeam("0-admin");
        adminTeam.setColor(ChatColor.DARK_RED);

        this.adminLiveTeam = scoreboard.registerNewTeam("1-admin_live");
        adminLiveTeam.setColor(ChatColor.DARK_RED);
        adminLiveTeam.setPrefix("§5[Live] ");

        this.liveTeam = scoreboard.registerNewTeam("2-live");
        liveTeam.setPrefix("§5[Live] ");

        this.userTeam = scoreboard.registerNewTeam("3-user");
    }

    /**
     * Update header and footer for all players.
     *
     * @param players current count of online players
     */
    public void updateHeaderAndFooter(int players) {
        String footer = ScoreboardManager.FOOTER.replace("%count%", players + "");

        IChatBaseComponent headerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \""
                + ScoreboardManager.HEADER + "\"}");
        IChatBaseComponent footerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.footer = footerComponent;
        packet.header = headerComponent;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Update the claimed chunks counter for a player.
     *
     * @param player player
     */
    public void updateChunkCounter(Player player) {
        objective.getScore(player.getName()).setScore(ClaimInformation.get(player.getUniqueId()).getChunks().size());

        player.setScoreboard(scoreboard);
    }

    /**
     * Update the tablist colors.
     *
     * @param player player
     * @param liveStatus true if the player is live, otherwise false
     */
    public void updateColors(Player player, boolean liveStatus) {
        removeEntry(adminLiveTeam, player.getName());
        removeEntry(liveTeam, player.getName());

        removeEntry(adminTeam, player.getName());
        removeEntry(userTeam, player.getName());

        if (liveStatus) {
            if (player.isOp()) {
                adminLiveTeam.addEntry(player.getName());
            } else {
                liveTeam.addEntry(player.getName());
            }
        } else {
            if (player.isOp()) {
                adminTeam.addEntry(player.getName());
            } else {
                userTeam.addEntry(player.getName());
            }
        }

        player.setScoreboard(scoreboard);
    }

    /**
     * Check if a player is already live.
     *
     * @param player player
     * @return true if the player is live, otherwise false
     */
    public boolean isLive(Player player) {
        return adminLiveTeam.hasEntry(player.getName()) || liveTeam.hasEntry(player.getName());
    }

    /**
     * Remove the entry from the team, if the team contains the entry.
     *
     * @param team team
     * @param entry entry to remove
     */
    private void removeEntry(Team team, String entry) {
        if (team.hasEntry(entry)) {
            team.removeEntry(entry);
        }
    }

    /**
     * Unregister the team.
     *
     * @param name team name
     */
    private void unregisterTeam(String name) {
        Team team = scoreboard.getTeam(name);

        if (team != null) {
            team.unregister();
        }
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
