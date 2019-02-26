package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.scoreboard.ScoreboardManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * This sub command is called, if a player wants to unclaim a chunk.
 *
 * @author Paul2708
 */
public class UnClaimCommand extends SubCommand {

    /**
     * Create a new unclaim command.
     */
    public UnClaimCommand() {
        super("unclaim", "unclaim", "Unclaime den aktuellen Chunk", SubCommand.NONE_PERMISSION);
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        ClaimInformation information = ClaimInformation.get(player.getUniqueId());
        ChunkData chunkData = new ChunkData(player.getLocation().getChunk());

        if (information != null && information.contains(chunkData)) {
            if (args.length == 1) {
                if (!player.hasMetadata("unclaim")) {
                    return;
                }

                if (args[0].equals("confirm")) {
                    // Unclaim the chunk
                    try {
                        information.updateChunk(chunkData, false);
                        ClaimPlugin.getInstance().getDatabase().updateClaimInformation(information.getUuid(), chunkData, false);

                        ScoreboardManager.getInstance().updateChunkCounter(player);

                        player.sendMessage(ClaimPlugin.PREFIX + "§6Der Chunk wurde entfernt.");
                    } catch (DatabaseException e) {
                        e.printStackTrace();

                        player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk konnte nicht entfernt werden.");
                    }
                } else if (args[0].equals("cancel")) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Du hast den Vorgang abgebrochen.");
                }

                player.removeMetadata("unclaim", ClaimPlugin.getInstance());
            } else {
                player.sendMessage(ClaimPlugin.PREFIX + "§7Bist du dir §6sicher§7, dass du §6diesen Chunk §7unclaimen "
                        + "willst?");
                player.sendMessage(ClaimPlugin.PREFIX + "Hinweis: Du bekommst §c§lkeinen §7Claimer zurück.");

                TextComponent yesMessage = new TextComponent("§a[Ja, ich will unclaimen]");
                yesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chunk unclaim confirm"));
                yesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7Unclaime den Chunk ohne Rückersttattung").create()));

                TextComponent noMessage = new TextComponent("§c[Nein, ich breche ab]");
                noMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chunk unclaim cancel"));
                noMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7Breche den Vorgang ab").create()));

                player.spigot().sendMessage(new ComponentBuilder(ClaimPlugin.PREFIX)
                        .append(yesMessage).append("   ").append(noMessage).create());

                player.setMetadata("unclaim", new FixedMetadataValue(ClaimPlugin.getInstance(), true));
            }
        } else {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDir gehört der Chunk nicht.");
        }
    }
}
