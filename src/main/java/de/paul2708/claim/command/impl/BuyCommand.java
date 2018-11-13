package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This sub command is called, if a player wants to buy a new claimer.
 *
 * @author Paul2708
 */
public class BuyCommand extends SubCommand {

    /**
     * Create a new buy command.
     */
    public BuyCommand() {
        super("buy", "buy", "Kaufe dir einen Claimer", SubCommand.NONE_PERMISSION);
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        ClaimInformation information = ClaimInformation.get(player.getUniqueId());
        int price = Utility.getPrice(information.getBuyLevel());
        int count = Utility.count(player, Material.DIAMOND);

        if (count < price) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast leider nicht genug Diamanten um dir einen Chunk kaufen "
                    + "zu können. §7[§6" + count + "§7/§6" + price + "§7]");
            return;
        }

        // Remove items and buy the claimer
        information.addLevel();

        try {
            ClaimPlugin.getInstance().getDatabase().save(player.getUniqueId());
        } catch (DatabaseException e) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cEin Fehler ist aufgetreten...");

            e.printStackTrace();
            return;
        }

        Utility.removeItems(player, Material.DIAMOND, price);

        ItemStack claimer = Utility.buildClaimer(player);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), claimer);
        } else {
            player.getInventory().addItem(claimer);
        }

        player.sendMessage(ClaimPlugin.PREFIX + "Du hast dir einen §6Claimer §7gekauft.");
    }
}
