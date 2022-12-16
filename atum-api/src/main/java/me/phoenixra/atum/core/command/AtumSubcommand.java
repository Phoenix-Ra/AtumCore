package me.phoenixra.atum.core.command;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AtumSubcommand extends AtumCommand{
    /**
     * Create a new command.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param permission  The permission required to execute the command.
     */
    protected AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull String permission) {
        super(plugin, commandName, permission);
    }
    /**
     * Create a new command.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param parent  the parent of a subcommand
     */
    protected AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull CommandBase parent) {
        super(plugin, commandName, parent.getRequiredPermission());
        this.setConsoleAllowed(parent.isConsoleAllowed());
        this.setPlayersAllowed(parent.isPlayersAllowed());
    }
}
