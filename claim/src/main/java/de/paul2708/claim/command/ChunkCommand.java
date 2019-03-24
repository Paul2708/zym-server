package de.paul2708.claim.command;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This command executor handles the <code>chunk</code> command.
 *
 * @author Paul2708
 */
public class ChunkCommand implements CommandExecutor, TabCompleter {

    private List<SubCommand> subCommands;

    /**
     * Create a new claim command with some sub commands.
     */
    public ChunkCommand() {
        this.subCommands = new ArrayList<>();

        this.subCommands.add(new InfoCommand());
        this.subCommands.add(new BuyCommand());
        this.subCommands.add(new ClaimCommand());
        this.subCommands.add(new UnClaimCommand());
        this.subCommands.add(new OpInfoCommand());
        this.subCommands.add(new PlayerInfoCommand());
        this.subCommands.add(new RemoveCommand());
        this.subCommands.add(new GiveClaimerCommand());
        this.subCommands.add(new BypassCommand());
        this.subCommands.add(new CityCommand());
        this.subCommands.add(new VersionCommand());
        this.subCommands.add(new HelpCommand(this.subCommands));
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
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk help §7für eine Übersicht aller Befehle.");
        } else {
            for (SubCommand subCommand : this.subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    if (subCommand.getPermission().equals(SubCommand.NONE_PERMISSION) || player.isOp()
                            || player.hasPermission(subCommand.getPermission())) {
                        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

                        subCommand.execute(player, subArgs);
                    } else {
                        sender.sendMessage(ClaimPlugin.PREFIX + "§cDu hast keine Rechte dafür.");
                    }

                    return true;
                }
            }

            // Send help message
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk help §7für eine Übersicht aller Befehle.");
        }

        return true;
    }

    /**
     * Added sub command suggestion.
     *
     * @param sender command sender
     * @param command command
     * @param alias alias
     * @param args arguments
     * @return list of command suggestions
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            List<String> suggestions = new LinkedList<>();

            if (args.length == 1) {
                for (SubCommand subCommand : subCommands) {
                    if (subCommand.getName().startsWith(args[0])) {
                        if (subCommand.getPermission().equals(SubCommand.NONE_PERMISSION) || player.isOp()
                                || player.hasPermission(subCommand.getPermission())) {
                            suggestions.add(subCommand.getName());
                        }
                    }
                }
            } else if (args.length > 1) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().startsWith(args[args.length - 1])) {
                        suggestions.add(onlinePlayer.getName());
                    }
                }
            }

            return suggestions;
        } else {
            return Collections.emptyList();
        }
    }
}
