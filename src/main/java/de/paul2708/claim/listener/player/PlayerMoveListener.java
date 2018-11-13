package de.paul2708.claim.listener.player;

import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This listener is called, if a player moves.
 *
 * @author Paul2708
 */
public class PlayerMoveListener implements Listener {

    /**
     * Send information about the chunk.
     *
     * @param event player move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        ChunkData fromChunk = new ChunkData(event.getFrom().getChunk());
        ChunkData toChunk = new ChunkData(event.getTo().getChunk());

        if (fromChunk.equals(toChunk) || sameType(fromChunk, toChunk)) {
            return;
        }

        Player player = event.getPlayer();

        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.getChunks().contains(toChunk)) {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(information.getUuid());
                String name;
                if (owner == null || owner.getName() == null || owner.getName().equals("null")) {
                    name = "jemandem";
                } else {
                    name = owner.getName();
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§7Chunk von §6" + name));
                return;
            }
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText("§7Unclaimed Chunk"));
    }

    /**
     * Check if two chunks have the same type.
     *
     * @param from from chunk
     * @param to to chunk
     * @return true if the types are the same, otherwise false
     */
    private boolean sameType(ChunkData from, ChunkData to) {
        boolean fromFree = true;
        boolean toFree = true;

        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(from)) {
                fromFree = false;
            }
            if (information.contains(to)) {
                toFree = false;
            }

            if (information.contains(from) && information.contains(to)) {
                return true;
            }
        }

        return fromFree && toFree;

    }
}
