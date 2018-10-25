package de.paul2708.claim.database.result;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This database result is used, if a player tries to claim a chunk.
 *
 * @author Paul2708
 */
public class ClaimResult implements DatabaseResult<Void> {

    private Player player;
    private Material material;
    private int amount;

    /**
     * Create a new claim result with player, material and amount.
     *
     * @param player player
     * @param material material
     * @param amount amount
     */
    public ClaimResult(Player player, Material material, int amount) {
        this.player = player;
        this.material = material;
        this.amount = amount;
    }

    /**
     * Successful received the result.
     *
     * @param result result
     */
    @Override
    public void success(Void result) {
        this.removeItems();

        player.sendMessage(ClaimPlugin.PREFIX + "§6Du hast erfolgreich den Chunk geclaimed.");
    }

    /**
     * Handle a database exception, if one occurred.
     *
     * @param exception database exception
     */
    @Override
    public void exception(DatabaseException exception) {
        player.sendMessage(ClaimPlugin.PREFIX + "§cEs ist ein Fehler aufgetreten. Reconnecte und versuche "
                + "es erneut.");

        exception.printStackTrace();
    }

    /**
     * Remove the items from the inventory.
     */
    private void removeItems() {
        int current = amount;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                if (current - content.getAmount() > 0) {
                    player.getInventory().remove(content);

                    current -= content.getAmount();
                } else {
                    content.setAmount(content.getAmount() - current);
                    return;
                }
            }
        }
    }
}
