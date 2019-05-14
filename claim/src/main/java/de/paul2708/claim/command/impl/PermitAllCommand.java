package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This sub command is called, if a player wants to permit a player on all chunks.
 *
 * @author Paul2708
 */
public class PermitAllCommand extends SubCommand {

    /**
     * Create a new permit all command.
     */
    public PermitAllCommand() {
        super("permitall", "permitall [Spieler]", "Erlaube einem Spieler auf allen Chunks zu bauen",
                SubCommand.NONE_PERMISSION);
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk permitall [Spieler]");
            return;
        }

        ProfileManager manager = ProfileManager.getInstance();
        Database database = ClaimPlugin.getInstance().getDatabase();
        Chunk chunk = player.getLocation().getChunk();

        switch (manager.getClaimType(chunk)) {
            case PLAYER:
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§c'" + args[0] + "' muss online sein.");
                    return;
                }

                ClaimProfile ownerProfile = manager.getProfile(player);
                ClaimProfile targetProfile = manager.getProfile(target);

                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDu kannst dir nicht selbst Rechte geben.");
                    return;
                }
                List<ChunkData> chunks = getMissingChunks(targetProfile, ownerProfile);

                if (chunks.size() == 0) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§c" + target.getName()
                            + " hat bereits Rechte auf all deinen Chunks.");
                    return;
                }

                for (ChunkData chunkData : chunks) {
                    database.addAccess(targetProfile.getId(), chunkData.getId(), new DatabaseResult<Integer>() {

                        @Override
                        public void success(Integer result) {
                            manager.getProfile(target.getUniqueId()).addAccess(chunkData);
                            manager.clearAccess(chunk);
                        }

                        @Override
                        public void exception(DatabaseException exception) {
                            player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                            exception.printStackTrace();
                        }
                    });
                }

                target.sendMessage(ClaimPlugin.PREFIX + "§6" + player.getName() + " §7hat dir Rechte auf all seinen Gruppen-Chunks gegeben.");
                player.sendMessage(ClaimPlugin.PREFIX + "Du hast §6" + target.getName() + " §7Rechte auf all deinen Gruppen-Chunks gegeben.");
                break;
            case CITY:
                player.sendMessage(ClaimPlugin.PREFIX + "§cDir gehört der Chunk nicht.");
                break;
            case UNCLAIMED:
                player.sendMessage(ClaimPlugin.PREFIX + "§cDir gehört der Chunk nicht.");
                break;
            default:
                throw new IllegalStateException("Enum type not handled.");
        }
    }

    /**
     * Get all missing chunks.
     *
     * @param targetProfile target profile
     * @param ownerProfile owner profile
     * @return list of chunks
     */
    private List<ChunkData> getMissingChunks(ClaimProfile targetProfile, ClaimProfile ownerProfile) {
        List<ChunkData> list = new ArrayList<>();

        for (ChunkData claimedChunk : ownerProfile.getClaimedChunks()) {
            if (claimedChunk.isGroupChunk() && !targetProfile.getAccess().contains(claimedChunk)) {
                list.add(claimedChunk);
            }
        }

        return list;
    }
}
