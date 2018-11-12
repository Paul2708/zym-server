package de.paul2708.claim.util;

import net.minecraft.server.v1_13_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class provides some utility methods like math-functions and help methods.
 *
 * @author Paul2708
 */
public final class Utility {

    private static List<Integer> PRICES = Arrays.asList(0, 8, 16, 32, 64, 64, 128, 128, 192);

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
                    player.getInventory().remove(content);

                    current -= content.getAmount();
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
     * Build the claimer with nbt owner tag.
     *
     * @param uuid player uuid
     * @return claimer item stack
     */
    public static ItemStack buildClaimer(UUID uuid) {
        // Build item stack
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§6Claimer");
        itemMeta.setLore(Arrays.asList(" ", "§7Nutze §6/chunk claim §7um einen Chunk zu claimen.",
                "§7Oder §6rechtsklicke §7in einem freien Chunk."));
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        itemStack.setItemMeta(itemMeta);

        // Add NBT Tag
        net.minecraft.server.v1_13_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = nmsCopy.getTag() != null ? nmsCopy.getTag() : new NBTTagCompound();
        tag.setString("owner", uuid.toString());
        nmsCopy.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsCopy);
    }

    /**
     * Check if the player owns the claimer.
     *
     * @param claimer claimer item stack
     * @return true if the item is the claimer, otherwise false
     */
    public static boolean isClaimer(ItemStack claimer) {
        if (claimer == null) {
            return false;
        }

        net.minecraft.server.v1_13_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(claimer);
        if (nmsCopy.getTag() == null) {
            return false;
        }

        return nmsCopy.getTag().hasKey("owner");
    }

    /**
     * Check if the player owns the claimer.
     *
     * @param uuid player uuid
     * @param claimer claimer item stack
     * @return true if the player owns the claimer, otherwise false
     */
    public static boolean ownsClaimer(UUID uuid, ItemStack claimer) {
        if (claimer == null) {
            return false;
        }

        net.minecraft.server.v1_13_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(claimer);
        if (nmsCopy.getTag() == null) {
            return false;
        }

        return nmsCopy.getTag().hasKey("owner") && nmsCopy.getTag().getString("owner").equals(uuid.toString());
    }
}
