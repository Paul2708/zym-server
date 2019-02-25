package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
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
        super("remove", "remove", "Entferne den aktuellen Chunk", "chunk.remove");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        ChunkData chunkData = new ChunkData(player.getLocation().getChunk());

        ClaimInformation marked = null;
        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(chunkData)) {
                marked = information;
            }
        }

        if (marked != null) {
            try {
                marked.updateChunk(chunkData, false);
                ClaimPlugin.getInstance().getDatabase().updateClaimInformation(marked.getUuid(), chunkData, false);

                // Update scoreboard
                Player target = Bukkit.getPlayer(marked.getUuid());
                if (target != null && target.isOnline()) {
                    ScoreboardManager.getInstance().update(target);
                }

                player.sendMessage(ClaimPlugin.PREFIX + "§6Der Chunk wurde entfernt.");
            } catch (DatabaseException e) {
                e.printStackTrace();

                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk konnte nicht entfernt werden.");
            }
        } else {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk ist nicht geclaimed.");
        }
    }
}
