package me.phoenixra.atum.core.command;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AtumCommand implements CommandBase, CommandExecutor, TabCompleter {

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
     * @param plugin       The plugin.
     * @param commandName  The name used in execution.
     * @param permission   The permission required to execute the command.
     */
    protected AtumCommand(@NotNull final AtumPlugin plugin,
                            @NotNull final String commandName,
                            @NotNull final String permission) {
        this.plugin=plugin;
        this.commandName=commandName;
        this.requiredPermission=permission;
        this.subcommands = new ArrayList<>();
    }

    public final void register() {
        PluginCommand command = Bukkit.getPluginCommand(this.getCommandName());
        if (command == null) return;

        command.setExecutor(this);
        command.setTabCompleter(this);

        if (this.getDescription() != null) {
            command.setDescription(this.getDescription());
        }

        List<String> aliases = new ArrayList<>(command.getAliases());
        aliases.addAll(this.getAliases());
        command.setAliases(aliases);

    }
    public final void unregister() {
        PluginCommand command = Bukkit.getPluginCommand(this.getCommandName());
        if (command == null) return;

        command.unregister(getCommandMap());
    }


    private List<String> getAliases() {
        return new ArrayList<>();
    }


    @Nullable
    public String getDescription() {
        return null;
    }
    /**
     * Internal implementation of the Bukkit method
     *
     * @param sender  The executor of the command.
     * @param command The bukkit command.
     * @param label   The name of the executed command.
     * @param args    The arguments of the command (anything after the physical command name)
     * @return true if command proceeded by the Atum core
     * @see CommandBase#onCommandExecute(CommandSender, List) --> use this instead <--
     */
    @Override
    public final boolean onCommand(@NotNull final CommandSender sender,
                                   @NotNull final Command command,
                                   @NotNull final String label,
                                   @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(this.getCommandName())) {
            getPlugin().getLogger().warning("onCommand executes inside the wrong class or " +
                    "the command name is wrongly specified! CommandName: "+getCommandName()
                    +"  Command name received: "+command.getName());
            return false;
        }

        handleCommand(sender,args);
        return true;
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

                if(subcommand instanceof AtumCommand)
                    ((AtumCommand) subcommand).handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                else
                    subcommand.onCommandExecute(sender, Arrays.asList(args));

                return;

            }
        }
        onCommandExecute(sender, Arrays.asList(args));
    }
    /**
     * Internal implementation of the Bukkit method
     *<p></p>
     * @param sender command sender
     * @param command command interface
     * @param label command label
     * @param args command args
     * @return suggested commands
     * @see CommandBase#onTabComplete(CommandSender, List) --> use this instead <--
     */
    @Override
    public final @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command,
                                                @NotNull final String label,
                                                @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(this.getCommandName())) {
            getPlugin().getLogger().warning("onTabComplete executes inside the wrong class or " +
                    "the command name is wrongly specified! CommandName: "+getCommandName()
                    +"  Command name received: "+command.getName());
            return null;
        }

        return this.handleTabCompletion(sender, args);
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

        if (!canExecute(sender)) return null;


        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0],
                    getSubcommands().stream()
                            .filter(subCommand -> subCommand.canExecute(sender))
                            .map(CommandBase::getCommandName)
                            .collect(Collectors.toList()),
                    completions
            );

            Collections.sort(completions);

            if (!completions.isEmpty()) {
                return completions;
            }
        }else if (args.length >= 2) {
            CommandBase command = null;

            for (CommandBase subcommand : getSubcommands()) {

                if (args[0].equalsIgnoreCase(subcommand.getCommandName())
                        && subcommand.canExecute(sender)) {

                    command = subcommand;
                }
            }

            if (command != null) {
                String[] subArgs=Arrays.copyOfRange(args, 1, args.length);

                if(command instanceof AtumCommand)
                    return ((AtumCommand)command).handleTabCompletion(sender, subArgs);
                else
                    return command.onTabComplete(sender,Arrays.asList(subArgs));
            }
        }

        return onTabComplete(sender, Arrays.asList(args));
    }



    /**
     * Get the internal server CommandMap.
     *
     * @return The CommandMap.
     */
    public static CommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new NullPointerException("Command map wasn't found!");
        }
    }

    @Override
    public @NotNull CommandBase addSubcommand(@NotNull CommandBase subcommand) {
        subcommands.add(subcommand);
        return subcommand;
    }

    @Override
    public @NotNull AtumPlugin getPlugin() {
        return plugin;
    }
}
