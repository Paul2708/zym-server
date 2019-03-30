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

/**
 * This sub command is called, if a player wants to permit a player on his group chunk.
 *
 * @author Paul2708
 */
public class PermitCommand extends SubCommand {

    /**
     * Create a new permit command.
     */
    public PermitCommand() {
        super("permit", "permit [Spieler]", "Erlaube einem Spieler auf deinem Chunk zu bauen", SubCommand.NONE_PERMISSION);
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
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk permit [Spieler]");
            return;
        }

        ProfileManager manager = ProfileManager.getInstance();
        Database database = ClaimPlugin.getInstance().getDatabase();
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
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDu musst einen Gruppen-Chunk haben um Spielern das Bauen zu erlauben.");
                    return;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§c'" + args[0] + "' muss online sein.");
                    return;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§cDu kannst dir nicht selbst Rechte geben.");
                    return;
                }
                if (manager.getProfile(target.getUniqueId()).getAccess().contains(chunkData)) {
                    player.sendMessage(ClaimPlugin.PREFIX + "§c" + target.getName() + " hat bereits Rechte auf deinem Chunk.");
                    return;
                }

                database.addAccess(manager.getProfile(target.getUniqueId()).getId(), chunkData.getId(), new DatabaseResult<Integer>() {

                    @Override
                    public void success(Integer result) {
                        manager.getProfile(target.getUniqueId()).addAccess(chunkData);
                        manager.clearAccess(chunk);

                        target.sendMessage(ClaimPlugin.PREFIX + "§6" + player.getName() + " §7hat dir Rechte auf seinem Gruppen-Chunk gegeben.");
                        player.sendMessage(ClaimPlugin.PREFIX + "Du hast §6" + target.getName() + " §7Rechte auf deinem Gruppen-Chunk gegeben.");
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                        exception.printStackTrace();
                    }
                });
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
}
