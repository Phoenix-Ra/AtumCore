package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumCommand
import me.phoenixra.atum.core.utils.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class PluginCommandAtum(
    plugin: AtumPlugin
): AtumCommand(plugin,"atum","atum.admin") {

    init {
        isPlayersAllowed = false
        isConsoleAllowed = true
        addSubcommand(SubcommandPlugin(plugin,this))
    }

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        sender.sendMessage(getHelp())
    }

    private fun getHelp(): String {
        return StringUtils.colorFormat(
            """
                ${ChatColor.GRAY}Available commands: 
                ${ChatColor.GREEN}atum plugin [action] ${ChatColor.GRAY}- useful command to manage the plugins without reloading the whole server
                """.trimIndent()
        )
    }
}