package de.paul2708.claim.listener.player;

import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkWrapper;
import de.paul2708.claim.model.chunk.CityChunk;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

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
        Player player = event.getPlayer();

        ChunkWrapper fromChunk = new ChunkWrapper(event.getFrom().getChunk());
        ChunkWrapper toChunk = new ChunkWrapper(event.getTo().getChunk());

        if (ItemManager.getInstance().isClaimer(event.getPlayer().getInventory().getItemInMainHand())) {
            this.drawBorder(event.getPlayer());
        }

        if (fromChunk.equals(toChunk) || sameType(event.getFrom().getChunk(), event.getTo().getChunk())) {
            return;
        }

        UUID uuid = getOwner(event.getTo().getChunk());
        String message;

        if (uuid == null) {
            message = "§7Unclaimed Chunk";
        } else {
            if (uuid.equals(CityChunk.OWNER)) {
                message = "§7Chunk von §6Stadt";
            } else {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(uuid);
                if (owner == null || owner.getName() == null || owner.getName().equals("null")) {
                    message = "§7Chunk von §6jemandem";
                } else {
                    message = "§7Chunk von §6" + owner.getName();
                }
            }
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    /**
     * Get the owner by chunk.
     *
     * @param chunk chunk
     * @return player uuid, city owner uuid, or null if the chunk is unclaimed
     */
    private UUID getOwner(Chunk chunk) {
        switch (ProfileManager.getInstance().getClaimType(chunk)) {
            case PLAYER:
                return ProfileManager.getInstance().getProfile(chunk).getUuid();
            case CITY:
                return CityChunk.OWNER;
            case UNCLAIMED:
                return null;
            default:
                throw new IllegalStateException("Unsupported enum type");
        }
    }

    /**
     * Check if two chunks have the same type.
     *
     * @param from from chunk
     * @param to to chunk
     * @return true if the types are the same, otherwise false
     */
    private boolean sameType(Chunk from, Chunk to) {
        ProfileManager manager = ProfileManager.getInstance();

        // Both chunks unclaimed
        if (!manager.isClaimed(from) && !manager.isClaimed(to)) {
            return true;
        }

        // Same owner
        if (manager.hasSameOwner(from, to)) {
            return true;
        }

        return false;
    }

    /**
     * Draw a chunk border.
     *
     * @param player player
     */
    private void drawBorder(Player player) {
        Chunk playerChunk = player.getLocation().getChunk();
        int[] positions = new int[] { 0, 15 };
        int y = player.getLocation().getBlockY();

        for (int i : positions) {
            for (int j : positions) {
                Block corner = playerChunk.getBlock(i, y - 3, j);
                Block upper = playerChunk.getBlock(i, y + 10, j);

                this.drawLine(player, corner, upper);
            }
        }

        this.drawLine(player, playerChunk.getBlock(0, y + 10, 0), playerChunk.getBlock(0, y + 10, 15));
        this.drawLine(player, playerChunk.getBlock(0, y + 10, 0), playerChunk.getBlock(15, y + 10, 0));
        this.drawLine(player, playerChunk.getBlock(15, y + 10, 15), playerChunk.getBlock(0, y + 10, 15));
        this.drawLine(player, playerChunk.getBlock(15, y + 10, 15), playerChunk.getBlock(15, y + 10, 0));
    }

    /**
     * Draw a line from a block to another.
     *
     * @param player player
     * @param first first block
     * @param second second block
     */
    private void drawLine(Player player, Block first, Block second) {
        Location firstLocation = first.getLocation().clone();

        Vector vector = second.getLocation().toVector().subtract(firstLocation.toVector());

        for (double i = 1; i <= firstLocation.distance(second.getLocation()); i += 0.75D) {
            vector.multiply(i);

            firstLocation.add(vector);

            Particle.DustOptions options = new Particle.DustOptions(Color.BLUE, 0.75f);
            player.spawnParticle(Particle.REDSTONE, firstLocation, 1, options);
            firstLocation.subtract(vector);
            vector.normalize();
        }
    }
}
