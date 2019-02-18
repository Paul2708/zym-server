package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This sub command is called, if a player wants more information about the current chunk.
 *
 * @author Paul2708
 */
public class OpInfoCommand extends SubCommand {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm");

    /**
     * Create a new op info command.
     */
    public OpInfoCommand() {
        super("opinfo", "opinfo", "Gib dir OP-Informationen über den Chunk", "chunk.opinfo");
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

                if (owner == null || owner.getLastPlayed() == 0L) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Der Spieler §6??? §7war noch nie online.");
                } else {
                    player.sendMessage(ClaimPlugin.PREFIX + "Der Spieler §6" + owner.getName() + " §7war zuletzt §6"
                            + OpInfoCommand.DATE_FORMAT.format(new Date(owner.getLastPlayed())) + " §7online.");
                }

                return;
            }
        }

        for (ClaimInformation information : ClaimInformation.getAll()) {
            for (ChunkData chunk : information.getChunks()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        ChunkData nextChunk = new ChunkData(chunk.getWorld(), chunk.getX() + x, chunk.getZ() + z);

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
