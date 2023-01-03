package me.phoenixra.atum.core.command;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.exceptions.NotificationException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface CommandBase {
    /**
     * Get command name.
     *
     * @return The name of the command.
     */
    @NotNull
    String getCommandName();

    /**
     * Get command description. Used in help method
     *
     * @return The description of a command
     */
    @NotNull
    String getDescription();

    /**
     * Get command usage. Used in help method
     *
     * @return The usage of a command
     */
    @NotNull
    String getUsage();

    /**
     * Get command permission.
     *
     * @return The permission of the command.
     */
    @NotNull
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
                        @NotNull final String[] args) throws NotificationException;
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
        if (!isPlayer && !isConsoleAllowed()) {
            sender.sendMessage("Command is for players only");
            return false;
        }
        if(!isPlayer) return true;
        //player
        if (!isPlayersAllowed()) {
            sender.sendMessage("Command is for console only");
            return false;
        }
        if (!sender.hasPermission(getRequiredPermission())) {
            sender.sendMessage("You don't have the permission to execute this command");
            return false;
        }

        return true;
    }

    /**
     * Basic notification method, that throws NotificationException
     * @param msg The msg to send
     * @param asLangKey if true -> msg is a key of a plugin's lang file message
     */
    default void notify(@NotNull String msg,
                        boolean asLangKey) throws NotificationException {

        throw new NotificationException(msg,asLangKey);
    }
    /**
     * throws NotificationException when obj is null
     * @param obj an object to check
     * @param msg The msg to send
     * @param asLangKey if true -> msg is a key of a plugin's lang file message
     */
    default @NotNull <T> T notifyNull(@Nullable T obj,
                                      @NotNull String msg,
                                      boolean asLangKey)
            throws NotificationException {

        if (obj==null) notify(msg,asLangKey);


        assert obj != null;
        return obj;
    }
    /**
     * throws NotificationException when predicate#test(obj) returns false
     * @param obj an object to check
     * @param predicate the condition to test on the received object
     * @param msg The msg to send
     * @param asLangKey if true -> msg is a key of a plugin's lang file message
     */
    default @NotNull <T> T notifyFalse(@NotNull T obj,
                                       @NotNull Predicate<T> predicate,
                                       @NotNull String msg,
                                       boolean asLangKey) throws NotificationException {
        notifyFalse(predicate.test(obj), msg,asLangKey);
        return obj;
    }
    /**
     * throws NotificationException when condition is false
     * @param condition boolean value
     * @param msg The msg to send
     * @param asLangKey if true -> msg is a key of a plugin's lang file message
     */
    default boolean notifyFalse(boolean condition,
                                @NotNull String msg,
                                boolean asLangKey) throws NotificationException {

        if (!condition) notify(msg,asLangKey);

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
