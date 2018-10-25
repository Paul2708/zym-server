package de.paul2708.claim.command.impl;

import de.paul2708.claim.command.SubCommand;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if an admin sets the item to buy a new chunk.
 *
 * @author Paul2708
 */
public class ItemCommand extends SubCommand {

    /**
     * Create a new item command.
     */
    public ItemCommand() {
        super("setitem", "setitem [Anzahl]", "das aktuelle Item als Währung zu setzten", "chunk.item");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        // TODO: Implement me
        System.out.println("item");
    }
}
