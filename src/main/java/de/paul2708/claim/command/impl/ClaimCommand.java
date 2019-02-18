package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.ClaimResponse;
import de.paul2708.claim.util.ItemManager;
import de.paul2708.claim.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This sub command is called, if a player wants to claim a chunk.
 *
 * @author Paul2708
 */
public class ClaimCommand extends SubCommand {

    /**
     * Create a new claim command.
     */
    public ClaimCommand() {
        super("claim", "claim", "Claime den aktuellen Chunk", SubCommand.NONE_PERMISSION);
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args   command arguments (<code>args[0]</code> is not the sub command)
     */
    @Override
    public void execute(Player player, String[] args) {
        // Check claimer
        boolean found = false;
        for (ItemStack itemStack : player.getInventory()) {
            if (ItemManager.getInstance().ownsClaimer(player.getUniqueId(), itemStack)) {
                found = true;
                break;
            }
        }

        if (!found) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast keinen Claimer im Inventar, der dir gehört.");
            return;
        }

        // Check world
        if (!player.getLocation().getChunk().getWorld().getName().equals(ClaimPlugin.MAIN_WORLD)) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDerzeit kann man im Nether nicht claimen.");
            return;
        }

        // Check chunk
        ClaimResponse response = Utility.canClaim(player, new ChunkData(player.getLocation().getChunk()));
        switch (response) {
            case ALREADY_CLAIMED:
                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk wurde bereits geclaimed.");
                return;
            case REGION:
                player.sendMessage(ClaimPlugin.PREFIX + "§cIm Chunk liegt eine geschützte Region.");
                return;
            case BORDER:
                player.sendMessage(ClaimPlugin.PREFIX
                        + "§cDer Chunk grenzt an einen Chunk, der bereits geclaimed ist.");
                return;
            case CLAIMABLE:
                break;
            default:
                break;
        }

        // Claim the chunk
        try {
            ClaimPlugin.getInstance().getDatabase().updateClaimInformation(player.getUniqueId(),
                    new ChunkData(player.getLocation().getChunk()), true);

            int index = -1;
            ItemStack replaced = null;

            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);

                if (ItemManager.getInstance().ownsClaimer(player.getUniqueId(), itemStack)) {
                    index = i;

                    if (itemStack.getAmount() == 1) {
                        replaced = new ItemStack(Material.AIR);
                    } else {
                        replaced = itemStack.clone();
                        replaced.setAmount(itemStack.getAmount() - 1);
                    }

                    break;
                }
            }

            player.getInventory().setItem(index, replaced);

            Utility.playEffect(player);

            player.sendMessage(ClaimPlugin.PREFIX + "Du hast den Chunk §6erfolgreich §7geclaimed.");

            Bukkit.broadcastMessage(ClaimPlugin.PREFIX + "§a§l" + player.getName()
                    + " §7hat seinen §e" + ClaimInformation.get(player.getUniqueId()).getChunks().size()
                    + ". Chunk §7geclaimed!");
        } catch (DatabaseException e) {
            e.printStackTrace();

            player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten...");
        }
    }
}
