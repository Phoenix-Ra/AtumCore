package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumCommand
import org.bukkit.command.CommandSender

class PluginCommandAtum(
    plugin: AtumPlugin
): AtumCommand(plugin,"atum","atum.admin") {

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        TODO("Not yet implemented")
    }
}