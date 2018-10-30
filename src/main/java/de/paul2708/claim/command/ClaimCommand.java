package de.paul2708.claim.command;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.impl.ItemCommand;
import de.paul2708.claim.command.impl.RemoveCommand;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This command executor handles the <code>claim</code> command.
 *
 * @author Paul2708
 */
public class ClaimCommand implements CommandExecutor {

    private List<SubCommand> subCommands;

    /**
     * Create a new claim command with some sub commands.
     */
    public ClaimCommand() {
        this.subCommands = new ArrayList<>();

        this.subCommands.add(new RemoveCommand());
        this.subCommands.add(new ItemCommand());
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ClaimPlugin.PREFIX + "§cNur Spieler koennen den Befehl benutzen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            this.executeMainCommand(player);
        } else {
            for (SubCommand subCommand : this.subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    if (player.isOp() || player.hasPermission(subCommand.getPermission())) {
                        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

                        subCommand.execute(player, subArgs);
                    } else {
                        sender.sendMessage(ClaimPlugin.PREFIX + "§cDu hast keine Rechte dafür.");
                    }

                    return true;
                }
            }

            // Send help message
            player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/claim §7um einen Chunk zu claimen.");

            for (SubCommand subCommand : subCommands) {
                if (player.isOp() || player.hasPermission(subCommand.getPermission())) {
                    player.sendMessage(ClaimPlugin.PREFIX + "Nutze §6/claim " + subCommand.getUsage() + "§7 um "
                            + subCommand.getDescription() + ".");
                }
            }
        }

        return true;
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
