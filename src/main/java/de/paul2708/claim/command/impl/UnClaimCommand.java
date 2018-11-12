package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants to unclaim a chunk.
 *
 * @author Paul2708
 */
public class UnClaimCommand extends SubCommand {

    /**
     * Create a new unclaim command.
     */
    public UnClaimCommand() {
        super("unclaim", "unclaim", "Unclaime den aktuellen Chunk", SubCommand.NONE_PERMISSION);
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        ClaimInformation information = ClaimInformation.get(player.getUniqueId());
        ChunkData chunkData = new ChunkData(player.getLocation().getChunk());

        if (information != null && information.contains(chunkData)) {
            try {
                information.updateChunk(chunkData, false);
                ClaimPlugin.getInstance().getDatabase().updateClaimInformation(information.getUuid(), chunkData, false);

                player.sendMessage(ClaimPlugin.PREFIX + "§6Der Chunk wurde entfernt.");
            } catch (DatabaseException e) {
                e.printStackTrace();

                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk konnte nicht entfernt werden.");
            }
        } else {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDir gehört der Chunk nicht.");
        }
    }

}
