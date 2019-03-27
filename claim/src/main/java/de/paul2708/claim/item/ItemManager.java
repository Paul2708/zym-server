package de.paul2708.claim.item;

import de.paul2708.claim.util.Utility;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
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

    private static final String TITLE = "§7Willst du einen §6Claimer §7kaufen?";

    private static ItemManager instance;

    /**
     * Create a new item manager.
     */
    private ItemManager() {

    }

    /**
     * Build the claimer with nbt owner and group tag.
     *
     * @param player player
     * @return claimer item stack
     */
    public ItemStack buildClaimer(Player player, boolean groupChunk) {
        // Build item stack
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        String name = "§6" + player.getName() + "'s §7" + (groupChunk ? "Gruppen-" : "") + "Claimer";
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(" ", "§7Nutze §6/chunk claim §7um einen Chunk zu claimen.",
                "§7Oder §6rechtsklicke §7in einem freien Chunk."));
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        itemStack.setItemMeta(itemMeta);

        // Add NBT Tag
        net.minecraft.server.v1_13_R2.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = nmsCopy.getTag() != null ? nmsCopy.getTag() : new NBTTagCompound();
        tag.setString("owner", player.getUniqueId().toString());
        tag.setBoolean("group", groupChunk);
        nmsCopy.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsCopy);
    }

    /**
     * Check if the player owns the claimer.
     *
     * @param claimer claimer item stack
     * @return true if the item is the claimer, otherwise false
     */
    public boolean isClaimer(ItemStack claimer) {
        if (claimer == null) {
            return false;
        }

        net.minecraft.server.v1_13_R2.ItemStack nmsCopy = CraftItemStack.asNMSCopy(claimer);
        if (nmsCopy.getTag() == null) {
            return false;
        }

        return nmsCopy.getTag().hasKey("owner");
    }

    /**
     * Check if the player owns the claimer and return the claimer type.
     *
     * @param uuid player uuid
     * @param claimer claimer item stack
     * @return true claimer type
     */
    public ClaimerType getClaimerType(UUID uuid, ItemStack claimer) {
        if (claimer == null) {
            return ClaimerType.NONE;
        }

        net.minecraft.server.v1_13_R2.ItemStack nmsCopy = CraftItemStack.asNMSCopy(claimer);
        NBTTagCompound tag = nmsCopy.getTag();
        if (tag == null) {
            return ClaimerType.NONE;
        }

        if (tag.hasKey("owner") && tag.getString("owner").equals(uuid.toString())) {
            if (tag.hasKey("group") && tag.getBoolean("group")) {
                return ClaimerType.GROUP;
            }

            return ClaimerType.NORMAL;
        }

        return  ClaimerType.NONE;
    }

    /**
     * Open the buy inventory.
     *
     * @param player player
     * @param price price
     * @param groupInventory true if you can buy group chunks, otherwise false
     */
    public void openInventory(Player player, int price, boolean groupInventory) {
        Inventory inventory = Bukkit.createInventory(null, 27, ItemManager.TITLE);

        // Create border
        ItemStack blackBorder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack yellowBorder = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);

        ItemMeta itemMeta = blackBorder.getItemMeta();
        itemMeta.setDisplayName(" ");

        blackBorder.setItemMeta(itemMeta);
        yellowBorder.setItemMeta(itemMeta);

        // Apply border
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, blackBorder);
        }

        inventory.setItem(0, yellowBorder);
        inventory.setItem(8, yellowBorder);
        inventory.setItem(inventory.getSize() - 9, yellowBorder);
        inventory.setItem(inventory.getSize() - 1, yellowBorder);

        // Set claimer
        ItemStack itemStack = buildClaimer(player, groupInventory);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList(" ", "§aKlicke um den Claimer zu kaufen.", "§7Kosten: §6"
                + (groupInventory ? Utility.GROUP_PRICE : price) + " §7Diamanten"));
        itemStack.setItemMeta(meta);

        inventory.setItem(13, itemStack);

        player.openInventory(inventory);
    }

    /**
     * Check if the given inventory view is the buy inventory.
     *
     * @param view inventory view
     * @return true if it's the buy inventory, otherwise false
     */
    public boolean isBuyInventory(InventoryView view) {
        if (view == null) {
            return false;
        }

        return view.getTitle().equals(ItemManager.TITLE);
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
