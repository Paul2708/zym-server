package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.CityChunk;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
        ProfileManager manager = ProfileManager.getInstance();
        Database database = ClaimPlugin.getInstance().getDatabase();
        Chunk chunk = player.getLocation().getChunk();

        switch (manager.getClaimType(chunk)) {
            case PLAYER:
                ClaimProfile profile = manager.getProfile(chunk);
                ChunkData chunkData = manager.getChunkData(chunk);

                database.removeChunk(chunkData.getId(), new DatabaseResult<Void>() {

                    @Override
                    public void success(Void result) {
                        profile.removeClaimedChunk(chunkData);

                        // Update scoreboard
                        Player target = Bukkit.getPlayer(profile.getUuid());
                        if (target != null && target.isOnline()) {
                            ScoreboardManager.getInstance().updateChunkCounter(target);
                        }

                        player.sendMessage(ClaimPlugin.PREFIX + "§6Der Chunk wurde entfernt.");
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                        exception.printStackTrace();
                    }
                });
                break;
            case CITY:
                CityChunk cityChunk = manager.getCityChunk(chunk);

                database.removeChunk(cityChunk.getChunkData().getId(), new DatabaseResult<Void>() {

                    @Override
                    public void success(Void result) {
                        manager.removeCityChunk(cityChunk);

                        player.sendMessage(ClaimPlugin.PREFIX + "§6Der Chunk wurde entfernt.");
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                        exception.printStackTrace();
                    }
                });
                break;
            case UNCLAIMED:
                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk ist nicht geclaimed.");
                return;
            default:
                throw new IllegalStateException("Enum type not handled.");
        }
    }
}
