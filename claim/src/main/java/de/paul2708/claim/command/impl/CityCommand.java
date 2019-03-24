package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.ChunkWrapper;
import de.paul2708.claim.model.chunk.CityChunk;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants claim a chunk as city chunk.
 *
 * @author Paul2708
 */
public class CityCommand extends SubCommand {

    /**
     * Create a new city command.
     */
    public CityCommand() {
        super("city", "city [true|false]", "Claime den aktuellen Chunk als Stadt-Chunk", "chunk.city");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 1) {
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk city [true/false]");
            return;
        }

        ProfileManager manager = ProfileManager.getInstance();
        Database database = ClaimPlugin.getInstance().getDatabase();
        Chunk chunk = player.getLocation().getChunk();

        switch (manager.getClaimType(chunk)) {
            case PLAYER:
                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk wurde bereits von einem Spieler geclaimt.");
                player.sendMessage(ClaimPlugin.PREFIX + "Falls du ihn trotzdem claimen möchtest, nutze davor §6/chunk remove§7.");
                break;
            case CITY:
                if (args.length == 1 && (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false"))) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk city [true/false]");
                    return;
                }

                CityChunk cityChunk = manager.getCityChunk(chunk);

                database.updateCityChunk(cityChunk.getId(), parseBoolean(args), new DatabaseResult<Void>() {

                    @Override
                    public void success(Void result) {
                        cityChunk.setInteractable(parseBoolean(args));

                        player.sendMessage(ClaimPlugin.PREFIX + "Der Stadt-Chunk wurde bearbeitet: §6"
                                + (parseBoolean(args) ? "Jeder" : "Keiner (ohne Bypass)") + " §6kann hier bauen.");
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                        exception.printStackTrace();
                    }
                });
                break;
            case UNCLAIMED:
                if (args.length == 1 && (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false"))) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk city [true/false]");
                    return;
                }

                ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);
                database.addCityChunk(CityChunk.OWNER_ID, chunkWrapper, parseBoolean(args), new DatabaseResult<Integer>() {

                    @Override
                    public void success(Integer result) {
                        CityChunk cityChunk = new CityChunk(new ChunkData(chunkWrapper, false), parseBoolean(args));
                        manager.addCityChunk(cityChunk);

                        player.sendMessage(ClaimPlugin.PREFIX + "Der Chunk wurde von §6Stadt §7geclaimed.");
                        player.sendMessage(ClaimPlugin.PREFIX + "§6" + (parseBoolean(args) ? "Jeder" : "Keiner (ohne Bypass) §7kann hier bauen."));
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");

                        exception.printStackTrace();
                    }
                });
                break;
            default:
                throw new IllegalStateException("Enum type not handled.");
        }
    }

    /**
     * Parse the boolean value.
     *
     * @param args arguments
     * @return boolean value
     */
    private boolean parseBoolean(String[] args) {
        if (args.length == 0) {
            return false;
        }

        return Boolean.parseBoolean(args[0]);
    }
}
