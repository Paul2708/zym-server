package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.util.ItemManager;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants a free claimer.
 *
 * @author Paul2708
 */
public class OpClaimCommand extends SubCommand {

    /**
     * Create a new op claim command.
     */
    public OpClaimCommand() {
        super("opclaim", "opclaim", "Bekomme einen Claimer umsonst", "chunk.opclaim");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(ItemManager.getInstance().buildClaimer(player));

        player.sendMessage(ClaimPlugin.PREFIX + "Du hast einen Claimer erhalten.");
    }
}
