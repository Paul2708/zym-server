package de.paul2708.claim.command;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command is called, if a player wants to trigger the live prefix.
 *
 * @author Paul2708
 */
public class LiveCommand implements CommandExecutor {

    /**
     * Execute the command.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            boolean live = !ScoreboardManager.getInstance().isLive(player);
            ScoreboardManager.getInstance().updateColors(player, live);

            player.sendMessage(ClaimPlugin.PREFIX + "Du bist nun "
                    + (live ? "" : "nicht mehr ") + "§alive§7.");
        } else {
            sender.sendMessage("[Claim] Der Befehl ist nur ingame ausfuehrbar.");
        }

        return true;
    }
}
