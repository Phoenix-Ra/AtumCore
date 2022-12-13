package com.phoenixra.atum.core.command;

import com.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AtumSubcommand extends CommandCraft{
    /**
     * Create a new subcommand.
     *
     * @param plugin      Instance of a plugin.
     * @param commandName The command name.
     * @param permission  The permission required to execute this command.
     */
    AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull String permission) {
        super(plugin, commandName, permission);

    }
    /**
     * Create a new subcommand.
     *
     * @param plugin      Instance of a plugin.
     * @param commandName The command name.
     * @param parent      The parent command
     */
    AtumSubcommand(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull CommandBase parent) {
        super(plugin, commandName, parent.getRequiredPermission());
        setConsoleAllowed(parent.isConsoleAllowed());
        setPlayersAllowed(parent.isPlayersAllowed());
    }
}
