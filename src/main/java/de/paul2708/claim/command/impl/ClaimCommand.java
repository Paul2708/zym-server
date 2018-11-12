package de.paul2708.claim.command.impl;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import org.bukkit.Chunk;
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
        System.out.println("lol claimed");
    }

    /**
     * Execute the main command.
     *
     * @param player player
     */
    private void executeMainCommand(Player player) {
        // Check inventory
        Material material = Material.matchMaterial(ClaimPlugin.getInstance().getConfiguration().get("item.type"));
        int amount = ClaimPlugin.getInstance().getConfiguration().get("item.amount");

        if (this.count(player, material) < amount) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDu hast leider nicht genug Items um dir einen Chunk kaufen zu "
                    + "können. §7[§6" + this.count(player, material) + "§7/§6" + amount + "§7]");
            return;
        }

        // Check region
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                BukkitAdapter.adapt(player.getWorld()));

        Chunk chunk = player.getLocation().getChunk();
        int bx = chunk.getX() << 4;
        int bz = chunk.getZ() << 4;
        BlockVector pt1 = new BlockVector(bx, 0, bz);
        BlockVector pt2 = new BlockVector(bx + 15, 256, bz + 15);

        ProtectedCuboidRegion region = new ProtectedCuboidRegion("ThisIsAnId", pt1, pt2);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(region);

        if (regions.size() > 0) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cDiesen Chunk kannst du nicht claimen.");
            return;
        }

        // Check chunk
        Database database = ClaimPlugin.getInstance().getDatabase();
        ChunkData chunkData = new ChunkData(player.getLocation().getChunk());

        try {
            if (database.isClaimed(chunkData)) {
                player.sendMessage(ClaimPlugin.PREFIX + "§cDer Chunk ist bereits geclaimed.");
                return;
            }

            database.updateClaimInformation(player.getUniqueId(), chunkData, true);

            this.removeItems(player, material, amount);

            player.sendMessage(ClaimPlugin.PREFIX + "§6Du hast erfolgreich den Chunk geclaimed.");
        } catch (DatabaseException e) {
            player.sendMessage(ClaimPlugin.PREFIX + "§cEs ist ein Fehler aufgetreten. Reconnecte und versuche "
                    + "es erneut.");

            e.printStackTrace();
        }
    }

    /**
     * Remove the items from the inventory.
     *
     * @param player player
     * @param material material
     * @param amount amount
     */
    private void removeItems(Player player, Material material, int amount) {
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

    /**
     * Count specific items in a players inventory.
     *
     * @param player player
     * @param material material
     * @return amount of specific items
     */
    private int count(Player player, Material material) {
        int amount = 0;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                amount += content.getAmount();
            }
        }

        return amount;
    }
}
