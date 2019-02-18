package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants information about the current chunk.
 *
 * @author Paul2708
 */
public class InfoCommand extends SubCommand {

    /**
     * Create a new info command.
     */
    public InfoCommand() {
        super("info", "info", "Gib dir Informationen über den Chunk", SubCommand.NONE_PERMISSION);
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

        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(chunkData)) {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(information.getUuid());
                String name = (owner == null || owner.getName() == null) ? "jemandem" : owner.getName();
                player.sendMessage(ClaimPlugin.PREFIX + "Der Chunk (§6" + chunkData.getX() + "§7, §6"
                        + chunkData.getZ() + "§7) gehört §6" + name + "§7.");
                return;
            }
        }

        for (ClaimInformation information : ClaimInformation.getAll()) {
            for (ChunkData chunk : information.getChunks()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        ChunkData nextChunk = new ChunkData(chunk.getX() + x, chunk.getZ() + z);

                        if (chunkData.equals(nextChunk)) {
                            player.sendMessage(ClaimPlugin.PREFIX + "Der Chunk (§6" + chunkData.getX() + "§7, §6"
                                    + chunkData.getZ() + "§7) ist frei, kann aber nicht geclaimed werden, "
                                    + "da er zwischen claimed Chunks liegt.");
                            return;
                        }
                    }
                }
            }
        }

        player.sendMessage(ClaimPlugin.PREFIX + "Der Chunk (§6" + chunkData.getX() + "§7, §6" + chunkData.getZ()
                + "§7) ist frei.");
    }

}
