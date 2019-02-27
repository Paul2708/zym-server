package de.paul2708.claim.command;

import de.paul2708.claim.ClaimPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command is called, if a player wants to be teleported to a near player.
 *
 * @author Paul2708
 */
public class TeleportHelpCommand implements CommandExecutor {

    private static final int DISTANCE = 20;

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

            if (args.length != 1) {
                player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/tphelp [Spieler]");
            } else {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null || !target.isOnline()) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDer Spieler ist offline.");
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cMan kann sich nicht zu sich selbst teleportieren.");
                    return true;
                }

                double distance = player.getLocation().distance(target.getLocation());
                if (distance > TeleportHelpCommand.DISTANCE) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§6" + target.getName() + " §7ist zu weit entfernt.");
                    player.sendMessage(ClaimPlugin.PREFIX + "Der Spieler muss innerhalb 20 Blöcke sein.");
                } else {
                    player.teleport(target);

                    player.sendMessage(ClaimPlugin.PREFIX + "Du wurdest teleportiert.");
                }
            }
        } else {
            sender.sendMessage("[Claim] Der Befehl ist nur ingame ausfuehrbar.");
        }

        return true;
    }
}
