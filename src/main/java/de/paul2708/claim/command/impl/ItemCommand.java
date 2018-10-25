package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This sub command is called, if an admin sets the item to buy a new chunk.
 *
 * @author Paul2708
 */
public class ItemCommand extends SubCommand {

    /**
     * Create a new item command.
     */
    public ItemCommand() {
        super("setitem", "setitem [Anzahl]", "das aktuelle Item als Währung zu setzten", "chunk.item");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/claim " + getUsage() + " §7um " + getDescription()
                    + ".");
            return;
        }

        // Check arguments
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ClaimPlugin.PREFIX + "§c'" + args[0] + "' ist keine gültige Zahl.");
            return;
        }

        if (amount < 0) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDie Anzahl der Items muss positiv sein.");
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cBitte halte ein Item in deiner Main-Hand.");
            return;
        }

        // Set the item
        ClaimPlugin.getInstance().getConfiguration().set("item.type", itemStack.getType().toString());
        ClaimPlugin.getInstance().getConfiguration().set("item.amount", amount);

        String information = amount + "x" + itemStack.getType().toString().toLowerCase().replace("_", " ");
        player.sendMessage(ClaimPlugin.PREFIX + "Du hast das Item nun auf §6" + information + " §7gesetzt.");
    }
}
