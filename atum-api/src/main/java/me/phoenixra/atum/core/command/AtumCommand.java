package me.phoenixra.atum.core.command;

import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AtumCommand extends CommandHandler implements CommandExecutor, TabCompleter {
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
        super(plugin, commandName, permission);
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

        this.handleCommand(sender, args);

        return true;
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
}
