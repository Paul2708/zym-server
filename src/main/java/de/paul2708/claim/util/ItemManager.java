package de.paul2708.claim.util;

import net.minecraft.server.v1_13_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

/**
 * This class represents the item manager and holds methods about items and inventories.
 *
 * @author Paul2708
 */
public final class ItemManager {

    private static ItemManager instance;

    private static Inventory inventory;

    /**
     * Create a new item manager.
     */
    private ItemManager() {

    }

    /**
     * Build the claimer with nbt owner tag.
     *
     * @param player player
     * @return claimer item stack
     */
    public static ItemStack buildClaimer(Player player) {
        // Build item stack
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§6" + player.getName() + "'s §7Claimer");
        itemMeta.setLore(Arrays.asList(" ", "§7Nutze §6/chunk claim §7um einen Chunk zu claimen.",
                "§7Oder §6rechtsklicke §7in einem freien Chunk."));
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        itemStack.setItemMeta(itemMeta);

        // Add NBT Tag
        net.minecraft.server.v1_13_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = nmsCopy.getTag() != null ? nmsCopy.getTag() : new NBTTagCompound();
        tag.setString("owner", player.getUniqueId().toString());
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

    /**
     * Open the buy inventory.
     *
     * @param player player
     * @param price price
     */
    public static void openInventory(Player player, int price) {
        if (ItemManager.inventory == null) {
            ItemManager.inventory = Bukkit.createInventory(null, 27, "§7Willst du einen §6Claimer §7kaufen?");

            // Create border
            ItemStack blackBorder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemStack yellowBorder = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);

            ItemMeta itemMeta = blackBorder.getItemMeta();
            itemMeta.setDisplayName(" ");

            blackBorder.setItemMeta(itemMeta);
            yellowBorder.setItemMeta(itemMeta);

            // Apply border
            for (int i = 0; i < ItemManager.inventory.getSize(); i++) {
                ItemManager.inventory.setItem(i, blackBorder);
            }

            ItemManager.inventory.setItem(0, yellowBorder);
            ItemManager.inventory.setItem(8, yellowBorder);
            ItemManager.inventory.setItem(ItemManager.inventory.getSize() - 9, yellowBorder);
            ItemManager.inventory.setItem(ItemManager.inventory.getSize() - 1, yellowBorder);

            // Set claimer
            ItemStack itemStack = ItemManager.buildClaimer(player);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(Arrays.asList(" ", "§aKlicke um den Claimer zu kaufen.", "§7Kosten: §6" + price
                    + " §7Diamanten"));
            itemStack.setItemMeta(meta);

            ItemManager.inventory.setItem(13, itemStack);
        }

        player.openInventory(inventory);
    }

    /**
     * Get the manager instance.
     *
     * @return manager instance
     */
    public static ItemManager getInstance() {
        if (ItemManager.instance == null) {
            ItemManager.instance = new ItemManager();
        }

        return instance;
    }
}
