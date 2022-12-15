package me.phoenixra.atum.plugin.commands;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.command.AtumCommand;
import org.jetbrains.annotations.NotNull;

public class PluginCommandAtum extends AtumCommand {
    /**
     * Create a new command.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param permission  The permission required to execute the command.
     */
    protected PluginCommandAtum(@NotNull AtumPlugin plugin, @NotNull String commandName, @NotNull String permission) {
        super(plugin, commandName, permission);
    }
}
