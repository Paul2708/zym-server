package de.paul2708.claim.util;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import de.paul2708.claim.model.chunk.ChunkData;
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

    /**
     * Price of one group chunk claimer.
     */
    public static final int GROUP_PRICE = 8;

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
     * Get the available group claims for a player.
     *
     * @param player player
     * @return amount of possible claims
     */
    public static boolean canBuyGroupClaimer(Player player) {
        ClaimProfile profile = ProfileManager.getInstance().getProfile(player);

        int normalChunks = 0;
        int groupChunks = 0;

        for (ChunkData claimedChunk : profile.getClaimedChunks()) {
            if (claimedChunk.isGroupChunk()) {
                groupChunks++;
            } else {
                normalChunks++;
            }
        }

        return 2 * normalChunks > groupChunks;
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
     * Play an effect.
     *
     * @param player player
     */
    public static void playEffect(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

        player.sendTitle("§aChunk geclaimed.", "§7Viel Spaß beim Bauen!", 10, 100, 20);

        for (int i = 0; i < 3; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(ClaimPlugin.getInstance(), () -> {
                randomFirework(player);
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
        meta.setPower(2);
        firework.setFireworkMeta(meta);

        return firework;
    }
}
