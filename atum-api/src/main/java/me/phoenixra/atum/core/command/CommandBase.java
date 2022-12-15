package me.phoenixra.atum.core.command;

import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface CommandBase {
    /**
     * Get command name.
     *
     * @return The name of the command.
     */
    String getCommandName();

    /**
     * Get command permission.
     *
     * @return The permission of the command.
     */
    String getRequiredPermission();

    /**
     * If console is allowed to use this command
     *
     * @return true if allowed.
     */
    boolean isConsoleAllowed();

    /**
     * If players are allowed to use this command
     *
     * @return true if allowed.
     */
    boolean isPlayersAllowed();

    /**
     * Checks whether the specified sender
     * is the player
     * <p></p>
     * Added for a convenience
     *
     * @return true if sender is a player
     */
    default boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }

    /**
     * Add a subcommand to the command.
     *
     * @param subcommand The subcommand.
     * @return The parent command.
     */
    @NotNull
    CommandBase addSubcommand(@NotNull CommandBase subcommand);

    /**
     * Handle the command.
     *
     * @param sender The sender.
     * @param args   The arguments.
     */
     void handleCommand(@NotNull final CommandSender sender,
                        @NotNull final String[] args);
    /**
     * Handle the tab completion.
     *
     * @param sender The sender.
     * @param args   The arguments.
     * @return The tab completion results.
     */
     List<String> handleTabCompletion(@NotNull final CommandSender sender,
                                      @NotNull final String[] args);
    /**
     *
     * @param sender The command sender.
     * @return true if sender can execute the command
     */
    default boolean canExecute(CommandSender sender){

        boolean isPlayer=isPlayer(sender);
        //console
        if (!isPlayer && isConsoleAllowed()) {
            //@TODO add the message to the sender
            return false;
        }
        if(!isPlayer) return true;
        //player
        if (!isPlayersAllowed()) {
            //@TODO add the message to the sender
            return false;
        }
        if (!sender.hasPermission(getRequiredPermission())) {
            //@TODO add the message to the sender
            return false;
        }

        return true;
    }

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();

}
