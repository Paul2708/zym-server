package de.paul2708.claim.listener.inventory;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.item.ClaimerType;
import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
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

        Player player = (Player) event.getWhoClicked();

        if (clickedInventory != null && ItemManager.getInstance().isBuyInventory(event.getView())) {
            event.setCancelled(true);

            ClaimerType type = ItemManager.getInstance().getClaimerType(player.getUniqueId(), event.getCurrentItem());

            if (type == ClaimerType.NONE) {
                return;
            }

            ClaimProfile profile = ProfileManager.getInstance().getProfile(player);

            // Check price
            int price = type == ClaimerType.NORMAL ? Utility.getPrice(profile.getClaimer()) : Utility.GROUP_PRICE;
            int count = Utility.count(player, Material.DIAMOND);

            if (count < price) {
                player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast leider nicht genug Diamanten um dir "
                        + "einen Chunk kaufen zu können. §7[§6" + count + "§7/§6" + price + "§7]");
                player.closeInventory();
                return;
            }

            if (type == ClaimerType.NORMAL) {
                Database database = ClaimPlugin.getInstance().getDatabase();
                database.setClaimer(profile.getId(), profile.getClaimer() + 1, new DatabaseResult<Void>() {

                    @Override
                    public void success(Void result) {
                        // Update claimer
                        profile.setClaimer(profile.getClaimer() + 1);

                        // Remove items and give claimer
                        Utility.removeItems(player, Material.DIAMOND, price);

                        ItemStack claimer = ItemManager.getInstance().buildClaimer(player, false);
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItemNaturally(player.getLocation(), claimer);
                        } else {
                            player.getInventory().addItem(claimer);
                        }

                        player.sendMessage(ClaimPlugin.PREFIX + "Du hast dir einen §6Claimer §7gekauft.");
                        player.closeInventory();
                    }

                    @Override
                    public void exception(DatabaseException exception) {
                        player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten.");
                        player.closeInventory();

                        exception.printStackTrace();
                    }
                });
                return;
            }
            if (type == ClaimerType.GROUP) {
                // Remove items and give claimer
                Utility.removeItems(player, Material.DIAMOND, price);

                ItemStack claimer = ItemManager.getInstance().buildClaimer(player, true);
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), claimer);
                } else {
                    player.getInventory().addItem(claimer);
                }

                player.sendMessage(ClaimPlugin.PREFIX + "Du hast dir einen §6Gruppen-Claimer §7gekauft.");
                player.closeInventory();
            }
        }
    }
}
