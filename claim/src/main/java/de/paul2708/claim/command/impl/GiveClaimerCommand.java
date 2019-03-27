package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This sub command is called, if a player wants a free claimer.
 *
 * @author Paul2708
 */
public class GiveClaimerCommand extends SubCommand {

    /**
     * Create a new give claimer command.
     */
    public GiveClaimerCommand() {
        super("giveclaimer", "giveclaimer [Name]", "Bekomme einen Claimer umsonst", "chunk.giveclaimer");
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Spieler ist offline.");
                return;
            }

            target.getInventory().addItem(ItemManager.getInstance().buildClaimer(target, false));

            target.sendMessage(ClaimPlugin.PREFIX + "Du hast einen Claimer erhalten.");
            player.sendMessage(ClaimPlugin.PREFIX + "§6" + target.getName() + " §7hat einen Claimer erhalten.");
        } else {
            player.sendMessage(ClaimPlugin.PREFIX + "§cGebe einen Spielernamen an.");
        }
    }
}
