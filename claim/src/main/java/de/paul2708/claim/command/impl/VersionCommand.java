package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants to get the current version.
 *
 * @author Paul2708
 */
public class VersionCommand extends SubCommand {

    /**
     * Create a new version command.
     */
    public VersionCommand() {
        super("version", "version", "Gebe dir die aktuelle Version aus", "chunk.version");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(ClaimPlugin.PREFIX + "Aktuelle Version: §6"
                + ClaimPlugin.getInstance().getDescription().getVersion());
    }
}