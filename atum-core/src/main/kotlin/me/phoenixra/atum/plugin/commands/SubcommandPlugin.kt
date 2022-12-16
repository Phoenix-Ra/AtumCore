package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumSubcommand
import me.phoenixra.atum.core.command.CommandBase
import me.phoenixra.atum.core.utils.PluginUtils
import me.phoenixra.atum.core.utils.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Event
import org.bukkit.plugin.*
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import java.net.URLClassLoader
import java.util.*

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
                PluginUtils.loadPlugin(args[1])

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


    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

    private fun getHelp(): String {
        return StringUtils.colorFormat(
            """
                ${ChatColor.GRAY}Available commands: 
                ${ChatColor.GREEN}atum plugin list ${ChatColor.GRAY}- sends the list of all loaded plugins
                ${ChatColor.GREEN}atum plugin info [plugin name] ${ChatColor.GRAY}- sends the plugin version, its authors and current status
                ${ChatColor.GREEN}atum plugin usage [plugin name] ${ChatColor.GRAY}- sends all commands plugin registered
                ${ChatColor.GREEN}atum plugin enable [plugin name] ${ChatColor.GRAY}- enables the plugin
                ${ChatColor.GREEN}atum plugin disable [plugin name] ${ChatColor.GRAY}- disables the plugin
                ${ChatColor.GREEN}atum plugin load [plugin name] ${ChatColor.GRAY}- loads the plugin from /plugins folder
                ${ChatColor.GREEN}atum plugin unload [plugin name] ${ChatColor.GRAY}- unloads the plugin
                ${ChatColor.GREEN}atum plugin restart [plugin name] ${ChatColor.GRAY}- unloads and loads the plugin
                """.trimIndent()
        );
    }

}