package de.paul2708.claim.command.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.command.SubCommand;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.ClaimResponse;
import de.paul2708.claim.util.ItemManager;
import de.paul2708.claim.util.Utility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

        /*player.sendMessage(ClaimPlugin.PREFIX + "§7Bist du dir §6sicher, dass du §6diesen Chunk §7claimen willst?");

        TextComponent yesMessage = new TextComponent("§a[Ja]");
        yesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "chunk help"));
        yesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§6Claime den Chunk (verbindlich)").create()));

        TextComponent noMessage = new TextComponent("§c[Nein]");
        noMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "chunk help"));
        noMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§6Breche den Vorgang ab").create()));

        player.spigot().sendMessage(new ComponentBuilder(yesMessage).append(" ").append(noMessage).create());*/

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
