package me.phoenixra.atum.core.command;

import me.phoenixra.atum.core.AtumPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandHandler implements CommandBase {

    private final AtumPlugin plugin;

    @Getter
    private final String commandName;

    @Getter
    private final String requiredPermission;

    @Getter
    private final List<CommandBase> subcommands;

    @Getter @Setter
    private boolean playersAllowed;
    @Getter @Setter
    private boolean consoleAllowed;

    /**
     * Create a new command.
     *
     * @param plugin      Instance of a plugin.
     * @param commandName The command name.
     * @param permission  The permission required to execute this command.
     */
    CommandHandler(@NotNull final AtumPlugin plugin,
                   @NotNull final String commandName,
                   @NotNull final String permission) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.requiredPermission = permission;
        this.subcommands = new ArrayList<>();
    }

    /**
     * Handle the command.
     *
     * @param sender The sender.
     * @param args   The arguments.
     */
    protected final void handleCommand(@NotNull final CommandSender sender,
                                @NotNull final String[] args) {
        if (!canExecute(sender)) {
            return;
        }

        if (args.length > 0) {
            for (CommandBase subcommand : this.getSubcommands()) {
                if (!subcommand.getCommandName().equalsIgnoreCase(args[0])) continue;
                if (!subcommand.canExecute(sender)) return;

                ((CommandHandler) subcommand).handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));

                return;

            }
        }
        onCommandExecute(sender, Arrays.asList(args));
    }

    /**
     * Handle the tab completion.
     *
     * @param sender The sender.
     * @param args   The arguments.
     * @return The tab completion results.
     */
    protected final List<String> handleTabCompletion(@NotNull final CommandSender sender,
                                                     @NotNull final String[] args) {

        if (!sender.hasPermission(this.getRequiredPermission())) {
            return null;
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0],
                    getSubcommands().stream()
                            .filter(subCommand -> subCommand.isPlayersAllowed() &&
                                    sender.hasPermission(subCommand.getRequiredPermission()))
                            .map(CommandBase::getCommandName)
                            .collect(Collectors.toList()),
                    completions
            );

            Collections.sort(completions);

            if (!completions.isEmpty()) {
                return completions;
            }
        }

        if (args.length >= 2) {
            CommandHandler command = null;

            for (CommandBase subcommand : getSubcommands()) {
                if (!sender.hasPermission(subcommand.getRequiredPermission())) {
                    continue;
                }

                if (args[0].equalsIgnoreCase(subcommand.getCommandName())) {
                    command = (CommandHandler) subcommand;
                }
            }

            if (command != null) {
                return command.handleTabCompletion(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        List<String> completions = new ArrayList<>(onTabComplete(sender, Arrays.asList(args)));
        if (sender instanceof Player player) {
            completions.addAll(onTabComplete(player, Arrays.asList(args)));
        }
        return completions;
    }

    /**
     * Add a subcommand to the command.
     *
     * @param subcommand The subcommand.
     * @return The parent command.
     */
    @Override
    public final CommandBase addSubcommand(@NotNull final CommandBase subcommand) {
        subcommands.add(subcommand);

        return this;
    }

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @Override
    public AtumPlugin getPlugin() {
        return this.plugin;
    }

}
