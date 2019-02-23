package de.paul2708.claim.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.model.ClaimResponse;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class provides some utility methods like math-functions and help methods.
 *
 * @author Paul2708
 */
public final class Utility {

    private static final List<Integer> PRICES = Arrays.asList(0, 8, 16, 32, 64, 64, 128, 128, 192);

    /**
     * Nothing to call here.
     */
    private Utility() {
        throw new IllegalAccessError();
    }

    /**
     * Get the price for the next chunk to claim.
     *
     * @param level current level
     * @return price
     */
    public static int getPrice(int level) {
        if (level >= Utility.PRICES.size()) {
            return Utility.PRICES.get(Utility.PRICES.size() - 1);
        }

        return Utility.PRICES.get(level);
    }

    /**
     * Check if a player can claim a chunk.<br>
     * A chunk can be claimed, if the chunk isn't claimed yet, if the chunks next to it is not claimed and there is
     * no region.
     *
     * @param player player
     * @param chunkData chunk to claim
     * @return claim response
     */
    public static ClaimResponse canClaim(Player player, ChunkData chunkData) {
        // Check chunk
        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(chunkData)) {
                return ClaimResponse.ALREADY_CLAIMED;
            }
        }

        // Check other chunks
        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.getUuid().equals(player.getUniqueId())) {
                continue;
            }

            for (ChunkData chunk : information.getChunks()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        ChunkData nextChunk = new ChunkData(chunk.getX() + x, chunk.getZ() + z);

                        if (chunkData.equals(nextChunk) && !hasChunkNextTo(player, chunkData)) {
                            return ClaimResponse.BORDER;
                        }
                    }
                }
            }
        }

        // Check existing regions
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                BukkitAdapter.adapt(player.getWorld()));

        Chunk chunk = player.getLocation().getChunk();
        int bx = chunk.getX() << 4;
        int bz = chunk.getZ() << 4;
        BlockVector pt1 = new BlockVector(bx, 0, bz);
        BlockVector pt2 = new BlockVector(bx + 15, 256, bz + 15);

        ProtectedCuboidRegion region = new ProtectedCuboidRegion("ThisIsAnId", pt1, pt2);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(region);

        if (regions.size() > 0) {
            return ClaimResponse.REGION;
        }

        return ClaimResponse.CLAIMABLE;
    }

    /**
     * Check if a player has the chunk next to it.
     *
     * @param player player
     * @param chunkData chunk to check
     * @return true if player owns it, otherwise false
     */
    private static boolean hasChunkNextTo(Player player, ChunkData chunkData) {
        for (ChunkData chunk : ClaimInformation.get(player.getUniqueId()).getChunks()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    ChunkData nextChunk = new ChunkData(chunk.getX() + x, chunk.getZ() + z);

                    if (chunkData.equals(nextChunk)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Remove the items from the inventory.
     *
     * @param player player
     * @param material material
     * @param amount amount
     */
    public static void removeItems(Player player, Material material, int amount) {
        int current = amount;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                if (current - content.getAmount() > 0) {
                    current -= content.getAmount();

                    content.setAmount(0);
                } else {
                    content.setAmount(content.getAmount() - current);
                    return;
                }
            }
        }
    }

    /**
     * Count specific items in a players inventory.
     *
     * @param player player
     * @param material material
     * @return amount of specific items
     */
    public static int count(Player player, Material material) {
        int amount = 0;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                amount += content.getAmount();
            }
        }

        return amount;
    }

    /**
     * Check if the player has a bypass.
     *
     * @param player player
     * @return true if the player has the bypass, otherwise false
     */
    public static boolean hasBypass(Player player) {
        return player.hasMetadata("bypass");
    }

    /**
     * Play an effect.
     *
     * @param player player
     */
    public static void playEffect(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

        player.sendTitle("§aChunk geclaimed.", "§7Viel Spaß beim Bauen!", 10, 100, 20);


        for (int i = 0; i < 3; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(ClaimPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    randomFirework(player);
                }
            }, i * 5L);
        }
    }

    /**
     * Spawn a random firework.
     *
     * @param player player
     * @return spawned firework
     */
    private static Firework randomFirework(Player player) {
        Random random = new Random();

        FireworkEffect.Builder builder = FireworkEffect.builder()
                .trail(random.nextBoolean())
                .flicker(random.nextBoolean());

        for (int i = 0; i < random.nextInt(8) + 3; i++) {
            builder.withColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
        builder.with(random.nextBoolean() ? FireworkEffect.Type.STAR
                : random.nextBoolean() ? FireworkEffect.Type.BALL
                : random.nextBoolean() ? FireworkEffect.Type.CREEPER : FireworkEffect.Type.BALL_LARGE);


        Firework firework = player.getLocation().getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(builder.build());
        meta.setPower(random.nextBoolean() ? 0 : 1);
        firework.setFireworkMeta(meta);

        return firework;
    }
}
