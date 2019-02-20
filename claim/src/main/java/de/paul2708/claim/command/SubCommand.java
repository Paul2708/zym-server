package de.paul2708.claim.command;

import org.bukkit.entity.Player;

/**
 * This class represents a sub command of the {@link ChunkCommand}.
 *
 * @author Paul2708
 */
public abstract class SubCommand {

    /**
     * None permission is needed.
     */
    public static final String NONE_PERMISSION = "NONE_PERMISSION";

    private final String name;
    private final String usage;
    private final String description;
    private final String permission;

    /**
     * Create a new sub command.
     *
     * @param name sub command name
     * @param usage command usage
     * @param description command description
     * @param permission command permission
     */
    public SubCommand(String name, String usage, String description, String permission) {
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    /**
     * Execute the sub command with arguments.
     *
     * @param player command sender
     * @param args command arguments (<code>args[0]</code> is not the sub command)
     */
    public abstract void execute(Player player, String[] args);

    /**
     * Get the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the usage.
     *
     * @return usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Get the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the permission.
     *
     * @return permission
     */
    public String getPermission() {
        return permission;
    }
}
