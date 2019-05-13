package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * This sub command is used, when a player wants to activate or disable the bypass.
 *
 * @author Leon
 */
public class BypassCommand extends SubCommand {

    /**
     * Create a new bypass command.
     */
    public BypassCommand() {
        super("bypass", "bypass", "(De-)Aktivere den Bypass", "chunk.bypass");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        if (player.hasMetadata("bypass")) {
            player.removeMetadata("bypass", ClaimPlugin.getInstance());
            player.sendMessage(ClaimPlugin.PREFIX + "Du hast den Bypass §cdeaktiviert§7.");
        } else {
            player.setMetadata("bypass", new FixedMetadataValue(ClaimPlugin.getInstance(), true));
            player.sendMessage(ClaimPlugin.PREFIX + "Du hast den Bypass §aaktiviert§7.");
        }
    }
}