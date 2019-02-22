package de.paul2708.elytra.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * This class holds some information about the spawn elytra item.
 *
 * @author Paul2708
 */
public class ItemManager {

    private ItemStack elytra;

    private ItemStack emptyItem;

    /**
     * Create the elytra.
     */
    public void createElytra() {
        this.elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();

        meta.setDisplayName("§eSpawn-Elytra");
        meta.setLore(Arrays.asList("", "§7Temporäre Elytra"));
        meta.setUnbreakable(true);

        elytra.setItemMeta(meta);

        this.emptyItem = new ItemStack(Material.AIR);
    }

    /**
     * Check, if the given item is the spawn elytra.
     *
     * @param item item to check, can be null
     * @return true if the item is the spawn elytra, otherwise false
     */
    public boolean isElytra(ItemStack item) {
        return item != null && elytra.isSimilar(item);
    }

    /**
     * Get the elytra item.
     *
     * @return elytra item
     */
    public ItemStack getElytra() {
        return elytra;
    }

    /**
     * Get an empty item.
     *
     * @return empty item
     */
    public ItemStack getEmptyItem() {
        return emptyItem;
    }
}
