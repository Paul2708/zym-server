package de.paul2708.claim.command.impl;

import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.ItemManager;
import de.paul2708.claim.util.Utility;
import org.bukkit.entity.Player;

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

        ItemManager.getInstance().openInventory(player, price);
    }
}