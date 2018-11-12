package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This sub command is a help command and shows the other commands.
 *
 * @author Paul2708
 */
public class HelpCommand extends SubCommand {

    private List<SubCommand> subCommands;

    /**
     * Create a new help command with a list of sub commands.
     *
     * @param list sub commands
     */
    public HelpCommand(List<SubCommand> list) {
        super("help", "help", "Zeige dir die Befehls-Übersicht an", SubCommand.NONE_PERMISSION);

        this.subCommands = list;
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(ClaimPlugin.PREFIX + "--- §6Befehl-Übersicht §7---");

        for (SubCommand subCommand : this.subCommands) {
            if (subCommand.getPermission().equals(SubCommand.NONE_PERMISSION)
                    || player.hasPermission(subCommand.getPermission())) {
                player.sendMessage(ClaimPlugin.PREFIX + "§7/chunk §6" + subCommand.getName() + "§8: "
                        + "§6" + subCommand.getDescription());
            }

        }

        player.sendMessage(ClaimPlugin.PREFIX + "--- §6Befehl-Übersicht §7---");
    }
}
