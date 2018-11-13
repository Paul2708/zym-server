package de.paul2708.claim.listener.player;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Utility;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This listener is called, if a player interacts.
 *
 * @author Paul2708
 */
public class PlayerInteractListener implements Listener {

    /**
     * Cancel the event, if the player doesn't own the chunk.<br>
     * And claim a chunk.
     *
     * @param event player interact event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

            if (ClaimInformation.isClaimedByOthers(player, chunk)) {
                event.setCancelled(true);
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            Chunk chunk = block == null ? player.getLocation().getChunk() : block.getChunk();

            if (ClaimInformation.isClaimedByOthers(player, chunk)) {
                event.setCancelled(true);
            }

            // Start claiming
            if (!Utility.isClaimer(player.getInventory().getItemInMainHand())) {
                return;
            }

            event.setCancelled(true);

            // Check claimer
            boolean found = false;
            for (ItemStack itemStack : player.getInventory()) {
                if (Utility.ownsClaimer(player.getUniqueId(), itemStack)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast keinen Claimer im Inventar, der dir gehört.");
                return;
            }

            // Check chunk
            if (!Utility.canClaim(player, new ChunkData(player.getLocation().getChunk()))) {
                player.sendMessage(ClaimPlugin.PREFIX + "§cDu kannst diesen Chunk nicht claimen.");
                return;
            }

            // Claim the chunk
            try {
                ClaimPlugin.getInstance().getDatabase().updateClaimInformation(player.getUniqueId(),
                        new ChunkData(player.getLocation().getChunk()), true);

                for (int i = 0; i < player.getInventory().getSize(); i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);

                    if (Utility.ownsClaimer(player.getUniqueId(), itemStack)) {
                        if (itemStack.getAmount() == 1) {
                            player.getInventory().setItem(i, new ItemStack(Material.AIR));
                        } else {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            player.getInventory().setItem(i, itemStack);
                        }

                        break;
                    }
                }

                player.sendMessage(ClaimPlugin.PREFIX + "Du hast den Chunk §6erfolgreich §7geclaimed.");
            } catch (DatabaseException e) {
                e.printStackTrace();

                player.sendMessage(ClaimPlugin.PREFIX + "§cEin Datenbank-Fehler ist aufgetreten...");
            }
        }
    }
}
