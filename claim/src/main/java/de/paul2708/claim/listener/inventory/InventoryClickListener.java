package de.paul2708.claim.listener.inventory;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.util.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player clicks into an inventory.
 *
 * @author Paul2708
 */
public class InventoryClickListener implements Listener {

    /**
     * Buy the claimer.
     *
     * @param event inventory click event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory openInventory = event.getInventory();

        Player player = (Player) event.getWhoClicked();

        if (clickedInventory != null && openInventory != null) {
            if (clickedInventory.getName().equals("§7Willst du einen §6Claimer §7kaufen?")
                    || openInventory.getName().equals("§7Willst du einen §6Claimer §7kaufen?")) {
                event.setCancelled(true);

                if (ItemManager.getInstance().isClaimer(event.getCurrentItem())) {
                    ClaimInformation information = ClaimInformation.get(player.getUniqueId());
                    int price = Utility.getPrice(information.getBuyLevel());
                    int count = Utility.count(player, Material.DIAMOND);

                    if (count < price) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast leider nicht genug Diamanten um dir "
                                + "einen Chunk kaufen zu können. §7[§6" + count + "§7/§6" + price + "§7]");
                        player.closeInventory();
                        return;
                    }

                    // Remove items and buy the claimer
                    information.addLevel();

                    try {
                        ClaimPlugin.getInstance().getDatabase().save(player.getUniqueId());
                    } catch (DatabaseException e) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Fehler ist aufgetreten...");
                        player.closeInventory();

                        e.printStackTrace();
                        return;
                    }

                    Utility.removeItems(player, Material.DIAMOND, price);

                    ItemStack claimer = ItemManager.getInstance().buildClaimer(player);
                    if (player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItemNaturally(player.getLocation(), claimer);
                    } else {
                        player.getInventory().addItem(claimer);
                    }

                    player.sendMessage(ClaimPlugin.PREFIX + "Du hast dir einen §6Claimer §7gekauft.");
                    player.closeInventory();
                }
            }
        }
    }
}
