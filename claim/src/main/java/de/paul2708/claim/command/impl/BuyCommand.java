package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.item.ItemManager;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
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
        ClaimProfile profile = ProfileManager.getInstance().getProfile(player);
        int price = Utility.getPrice(profile.getClaimer());

        if (args.length != 1 || (!args[0].equalsIgnoreCase("normal") && !args[0].equalsIgnoreCase("group"))) {
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/chunk buy [normal|group]");
            return;
        }

        ItemManager.getInstance().openInventory(player, price, args[0].equalsIgnoreCase("group"));
    }
}
