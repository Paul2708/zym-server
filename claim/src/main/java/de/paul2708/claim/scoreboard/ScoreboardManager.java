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
     * Update the scoreboard for a player. It will be set for everyone.
     *
     * @param player player
     */
    public void update(Player player) {
        // Chunks in tab
        objective.getScore(player.getName()).setScore(ClaimInformation.get(player.getUniqueId()).getChunks().size());

        // Admin color
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
