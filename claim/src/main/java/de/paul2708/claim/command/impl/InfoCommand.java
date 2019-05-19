package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.ChunkWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This sub command is called, if a player wants information about the current chunk.
 *
 * @author Paul2708
 */
public class InfoCommand extends SubCommand {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm");

    /**
     * Create a new info command.
     */
    public InfoCommand() {
        super("info", "info <Spieler>", "Gib dir Informationen über den Chunk oder Spieler", SubCommand.NONE_PERMISSION);
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

        ClaimProfile profile = null;

        if (!player.isOp() || args.length == 0) {
            // Chunk information
            Chunk chunk = player.getLocation().getChunk();

            player.sendMessage(ClaimPlugin.PREFIX + "Informationen zu diesem Chunk:");
            player.sendMessage(ClaimPlugin.PREFIX + "Koordinaten: §6" + chunk.getX() + "§7, §6" + chunk.getZ());
            player.sendMessage(ClaimPlugin.PREFIX + "Welt-Koordinaten: §6" + (chunk.getX() * 16) + "§7, §6?§7, §6"
                    + (chunk.getZ() * 16) + "");

            switch (manager.getClaimType(player.getLocation().getChunk())) {
                case PLAYER:
                    profile = manager.getProfile(chunk);
                    break;
                case CITY:
                    player.sendMessage(ClaimPlugin.PREFIX + "Besitzer: §6Stadt");
                    player.sendMessage(ClaimPlugin.PREFIX + "Bebaubar: §6"
                            + (manager.getCityChunk(chunk).isInteractable() ? "ja" : "nein"));
                    return;
                case UNCLAIMED:
                    for (ClaimProfile single : manager.getProfiles()) {
                        for (ChunkData singleChunk : single.getClaimedChunks()) {
                            for (int x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++) {
                                    ChunkWrapper nextChunk = new ChunkWrapper(chunk.getWorld().getName(), chunk.getX() + x,
                                            chunk.getZ() + z);

                                    if (singleChunk.getWrapper().equals(nextChunk) && !singleChunk.isGroupChunk()) {
                                        player.sendMessage(ClaimPlugin.PREFIX + "Besitzer: §6frei §7(protected)");
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    player.sendMessage(ClaimPlugin.PREFIX + "Besitzer: §6frei §7(unclaimed)");
                    return;
                default:
                    throw new IllegalStateException("Unhandled enum type");
            }
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (manager.doesProfileExist(target.getUniqueId())) {
                profile = manager.getProfile(target.getUniqueId());
            } else {
                player.sendMessage(ClaimPlugin.PREFIX + "§cEs konnte kein Profil zu " + args[0] + " gefunden werden.");
                return;
            }
        }

        // Get information
        OfflinePlayer owner = Bukkit.getOfflinePlayer(profile.getUuid());
        String name = owner.getName() == null ? "(keinen Namen gefunden)" : owner.getName();

        int[] counts = countClaimedChunks(profile);

        // Print information
        player.sendMessage(ClaimPlugin.PREFIX + "Besitzer: §6" + name);
        player.sendMessage(ClaimPlugin.PREFIX + "Gruppenchunk: §6"
                + (manager.getChunkData(player.getLocation().getChunk()).isGroupChunk() ? "§jJa" : "§6nein"));

        if (player.isOp()) {
            player.sendMessage(ClaimPlugin.PREFIX + "zuletzt online: §6"
                    + InfoCommand.DATE_FORMAT.format(new Date(owner.getLastPlayed())));
            player.sendMessage(ClaimPlugin.PREFIX + "Anzahl der (Gruppen-)Chunks: (§6"
                    + counts[0] + "§7) §6" + counts[1]);
            player.sendMessage(ClaimPlugin.PREFIX + "Anzahl der gekauften Claimer: §6"
                    + profile.getClaimer());
            player.sendMessage(ClaimPlugin.PREFIX + "Geclaimte Chunks:");

            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                ChunkWrapper wrapper = claimedChunk.getWrapper();
                player.sendMessage(ClaimPlugin.PREFIX + "  - §6" + wrapper.getX() + "§7, §6"
                        + wrapper.getZ());
            }

            player.sendMessage(ClaimPlugin.PREFIX + "Anzahl der Rechte auf Gruppen-Chunks: §6"
                    + profile.getAccess().size());
        }
    }

    /**
     * Count the claimed chunks.
     *
     * @param profile claim profile
     * @return int array: index 0 = group chunks, index 1 = normal chunks
     */
    private int[] countClaimedChunks(ClaimProfile profile) {
        int[] counts = new int[] { 0, 0 };

        for (ChunkData claimedChunk : profile.getClaimedChunks()) {
            counts[claimedChunk.isGroupChunk() ? 0 : 1]++;
        }

        return counts;
    }
}
