package me.phoenixra.atum.core.command;

import lombok.Getter;
import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AtumSubcommand extends AtumCommand{
    @Getter
    public final CommandBase parent;

    /**
     * Create a new subcommand.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param permission  The permission
     * @param parent      The parent of a subcommand
     */
    protected AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull String permission, @NotNull CommandBase parent) {
        super(plugin, commandName, permission);
        this.setConsoleAllowed(parent.isConsoleAllowed());
        this.setPlayersAllowed(parent.isPlayersAllowed());
        this.parent=parent;
    }

    /**
     * Create a new subcommand.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param parent      The parent of a subcommand
     */
    protected AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull CommandBase parent) {
        this(plugin,commandName,parent.getRequiredPermission(),parent);
    }

}
