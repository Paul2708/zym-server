package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This sub command is called, if a player wants to get a list of all its permitted players.
 *
 * @author Paul2708
 */
public class PermitListCommand extends SubCommand {

    /**
     * Create a new permit list command.
     */
    public PermitListCommand() {
        super("permitlist", "permitlist", "Zeige dir alle Spieler an, die auf deinem Chunk bauen können",
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
        ProfileManager manager = ProfileManager.getInstance();
        Chunk chunk = player.getLocation().getChunk();

        switch (manager.getClaimType(chunk)) {
            case PLAYER:
                ClaimProfile profile = manager.getProfile(chunk);
                ChunkData chunkData = manager.getChunkData(chunk);

                if (!profile.getUuid().equals(player.getUniqueId())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDir gehört der Chunk nicht.");
                    return;
                }
                if (!chunkData.isGroupChunk()) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDu musst einen Gruppen-Chunk haben um die die Spieler anzuzeigen.");
                    return;
                }

                List<OfflinePlayer> list = getPermits(chunkData);

                if (list.size() == 0) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cMomentan hat kein Spieler Rechte hier.");
                } else {
                    player.sendMessage(ClaimPlugin.PREFIX + "Folgende Spieler haben Rechte auf diesem Chunk:");

                    for (OfflinePlayer offlinePlayer : list) {
                        player.sendMessage(ClaimPlugin.PREFIX + "  - §6" + offlinePlayer.getName());
                    }
                }

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
     * Get a list of all permits.
     *
     * @param chunkData chunk data
     * @return list of permits
     */
    private List<OfflinePlayer> getPermits(ChunkData chunkData) {
        List<OfflinePlayer> list = new ArrayList<>();

        for (ClaimProfile profile : ProfileManager.getInstance().getProfiles()) {
            if (profile.getAccess().contains(chunkData)) {
               list.add(Bukkit.getOfflinePlayer(profile.getUuid()));
            }
        }

        return list;
    }
}
