package de.paul2708.claim.command;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.impl.ItemCommand;
import de.paul2708.claim.command.impl.RemoveCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This command executor handles the <code>claim</code> command.
 *
 * @author Paul2708
 */
public class ClaimCommand implements CommandExecutor {

    private List<SubCommand> subCommands;

    /**
     * Create a new claim command with some sub commands.
     */
    public ClaimCommand() {
        this.subCommands = new ArrayList<>();

        this.subCommands.add(new RemoveCommand());
        this.subCommands.add(new ItemCommand());
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ClaimPlugin.PREFIX + "§cNur Spieler koennen den Befehl benutzen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // TODO: Handle main claim command
        } else {
            for (SubCommand subCommand : this.subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    if (player.isOp() || player.hasPermission(subCommand.getPermission())) {
                        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

                        subCommand.execute(player, subArgs);
                    } else {
                        sender.sendMessage(ClaimPlugin.PREFIX + "§cDu hast keine Rechte dafür.");
                    }

                    return true;
                }
            }

            // Send help message
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/claim §7um einen Chunk zu claimen.");

            for (SubCommand subCommand : subCommands) {
                if (player.isOp() || player.hasPermission(subCommand.getPermission())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/claim " + subCommand.getUsage() + "§7 um"
                            + subCommand.getDescription() + ".");
                }
            }
        }

        return true;
    }
}
