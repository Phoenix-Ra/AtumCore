package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumSubcommand
import me.phoenixra.atum.core.command.CommandBase
import me.phoenixra.atum.core.utils.PluginUtils
import me.phoenixra.atum.core.utils.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.StringBuilder

class SubcommandPlugin(
    plugin: AtumPlugin,
    parent: CommandBase
) : AtumSubcommand(plugin, "plugin", parent) {


    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        notifyFalse(args.isNotEmpty(), getHelp(), false)

        when(args[0].lowercase()){
            "list" -> {
                val plugins=PluginUtils.getLoadedPlugins();
                val sb = StringBuilder()
                sb.append("${ChatColor.GRAY}Loaded Plugins(${plugins.size}): \n")
                for(plugin in PluginUtils.getLoadedPlugins()){
                    sb.append("$plugin, ")
                }
                sender.sendMessage(sb.toString())
            }
            "info" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                sender.sendMessage(PluginUtils.getPluginInfo(args[1]))
            }
            "usage" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.getLoadedPlugin(args[1])
                notifyNull(plugin,"&cThe specified plugin not found", false)

                sender.sendMessage(PluginUtils.getPluginUsage(plugin))
            }
            "enable" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.getLoadedPlugin(args[1])
                notifyNull(plugin,"&cThe specified plugin not found", false)

                PluginUtils.enablePlugin(plugin)
                sender.sendMessage("${ChatColor.GREEN}Successfully enabled the plugin ${plugin.name}")
            }
            "disable" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.getLoadedPlugin(args[1])
                notifyNull(plugin,"&cThe specified plugin not found", false)

                PluginUtils.disablePlugin(plugin)
                sender.sendMessage("${ChatColor.GREEN}Successfully disabled the plugin ${plugin.name}")
            }
            "load" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.loadPlugin(args[1])

                sender.sendMessage("${ChatColor.GREEN}Successfully loaded the plugin ${plugin.name}")
            }
            "unload" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.getLoadedPlugin(args[1])
                notifyNull(plugin,"&cThe specified plugin not found", false)

                PluginUtils.unloadPlugin(plugin)

                sender.sendMessage("${ChatColor.GREEN}Successfully unloaded the plugin ${plugin.name}")
            }
            "restart" -> {
                notifyFalse(args.size > 1, "&cSpecify the plugin", false)
                val plugin = PluginUtils.getLoadedPlugin(args[1])
                notifyNull(plugin,"&cThe specified plugin not found", false)

                PluginUtils.unloadPlugin(plugin)
                PluginUtils.loadPlugin(plugin.name)

                sender.sendMessage("${ChatColor.GREEN}Successfully restarted the plugin ${plugin.name}")
            }
            else -> {
                sender.sendMessage(getHelp())
            }
        }

    }

    override fun getDescription(): String {
        return "Useful command to manage the plugins without reloading the whole server"
    }

    override fun getUsage(): String {
        return "/atum plugin [action]"
    }


    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

    private fun getHelp(): String {
        return StringUtils.format(
            """
                &aAvailable commands: 
                &c/atum plugin list &7-&f sends the list of all loaded plugins
                &c/atum plugin info [plugin name] &7-&f sends the plugin version, its authors and current status
                &c/atum plugin usage [plugin name] &7-&f sends all commands plugin registered
                &c/atum plugin enable [plugin name] &7-&f enables the plugin
                &c/atum plugin disable [plugin name] &7-&f disables the plugin
                &c/atum plugin load [plugin name] &7-&f loads the plugin from /plugins folder
                &c/atum plugin unload [plugin name] &7-&f unloads the plugin
                &c/atum plugin restart [plugin name] &7-&f unloads and loads the plugin
                """.trimIndent()
        );
    }

}