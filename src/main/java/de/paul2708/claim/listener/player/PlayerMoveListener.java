package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.ItemManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

/**
 * This listener is called, if a player moves.
 *
 * @author Paul2708
 */
public class PlayerMoveListener implements Listener {

    private static final Location TELEPORT_FROM =
            new Location(Bukkit.getWorld("NewWorld"), 123.5, 69, 78.5);

    private static final Location TELEPORT_TO =
            new Location(Bukkit.getWorld("NewWorld"), 126.5, 232, 75.5);

    /**
     * Send information about the chunk and handle elytra stuff.
     *
     * @param event player move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Elytra stuff
        Location to = event.getTo();

        if (to.getWorld().equals(TELEPORT_FROM.getWorld())
                && Math.abs(to.getX() - TELEPORT_FROM.getX()) <= 0.2
                && Math.abs(to.getY() - TELEPORT_FROM.getY()) <= 0.5
                && Math.abs(to.getZ() - TELEPORT_FROM.getZ()) <= 0.2) {
            player.teleport(PlayerMoveListener.TELEPORT_TO);

            int freeSlot = player.getInventory().firstEmpty();
            if (freeSlot != -1) {
                player.getInventory().setItem(freeSlot, player.getInventory().getChestplate());
            }

            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            player.updateInventory();

            player.setMetadata("elytra", new FixedMetadataValue(ClaimPlugin.getInstance(), true));
        }

        // Chunk stuff
        ChunkData fromChunk = new ChunkData(event.getFrom().getChunk());
        ChunkData toChunk = new ChunkData(event.getTo().getChunk());

        if (ItemManager.getInstance().isClaimer(event.getPlayer().getInventory().getItemInMainHand())) {
            this.drawBorder(event.getPlayer());
        }

        if (fromChunk.equals(toChunk) || sameType(fromChunk, toChunk)) {
            return;
        }

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

                this.drawLine(corner, upper);
            }
        }

        this.drawLine(playerChunk.getBlock(0, y + 10, 0), playerChunk.getBlock(0, y + 10, 15));
        this.drawLine(playerChunk.getBlock(0, y + 10, 0), playerChunk.getBlock(15, y + 10, 0));
        this.drawLine(playerChunk.getBlock(15, y + 10, 15), playerChunk.getBlock(0, y + 10, 15));
        this.drawLine(playerChunk.getBlock(15, y + 10, 15), playerChunk.getBlock(15, y + 10, 0));
    }

    /**
     * Draw a line from a block to another.
     *
     * @param first first block
     * @param second second block
     */
    private void drawLine(Block first, Block second) {
        Location firstLocation = first.getLocation().clone();

        Vector vector = second.getLocation().toVector().subtract(firstLocation.toVector());

        for (double i = 1; i <= firstLocation.distance(second.getLocation()); i += 0.75D) {
            vector.multiply(i);

            firstLocation.add(vector);

            Particle.DustOptions options = new Particle.DustOptions(Color.BLUE, 0.75f);
            first.getLocation().getWorld().spawnParticle(Particle.REDSTONE, firstLocation, 1, options);
            firstLocation.subtract(vector);
            vector.normalize();
        }
    }
}
