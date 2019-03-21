package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

/**
 * This sub command is called, if a player wants information about a player.
 *
 * @author Paul2708
 */
public class PlayerInfoCommand extends SubCommand {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm");

    /**
     * Create a new op info command.
     */
    public PlayerInfoCommand() {
        super("playerinfo", "playerinfo [Spieler]", "Gibt dir Spieler-Informationen über den Chunk",
                "chunk.playerinfo");
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
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk playerinfo [Spieler]");
            return;
        }

        // TODO: Fix me

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        /*if (offlinePlayer == null || offlinePlayer.getUniqueId() == null
                || ClaimInformation.get(offlinePlayer.getUniqueId()) == null) {
            player.sendMessage(ClaimPlugin.PREFIX + "Es konnten keine Informationen über §6" + args[0]
                    + " §7gefunden werden.");
        } else {
            player.sendMessage(ClaimPlugin.PREFIX + "§6" + offlinePlayer.getName() + " §7war zuletzt §6"
                    + PlayerInfoCommand.DATE_FORMAT.format(new Date(offlinePlayer.getLastPlayed())) + " §7online.");

            player.sendMessage(ClaimPlugin.PREFIX + "Folgende Chunks hat der Spieler geclaimed:");
            for (ChunkData chunk : ClaimInformation.get(offlinePlayer.getUniqueId()).getChunks()) {
                player.sendMessage(ClaimPlugin.PREFIX + "  - §6X = " + chunk.getX() + ", Z = " + chunk.getZ());
            }
        }*/
    }
}
