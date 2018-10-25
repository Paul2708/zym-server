package de.paul2708.claim.command.impl;

import de.paul2708.claim.command.SubCommand;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if an admin removes a chunk.
 *
 * @author Paul2708
 */
public class RemoveCommand extends SubCommand {

    /**
     * Create a new remove command.
     */
    public RemoveCommand() {
        super("remove", "remove", "den aktuellen Chunk zu entfernen", "chunk.remove");
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
        System.out.println("remove");
    }
}
