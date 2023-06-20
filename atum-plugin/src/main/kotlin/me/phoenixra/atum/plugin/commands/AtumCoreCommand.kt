package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumCommand
import org.bukkit.command.CommandSender

class AtumCoreCommand(
    plugin: AtumPlugin
): AtumCommand(plugin,"atum","atum.admin") {

    init {
        addSubcommand(PluginSubcommand(plugin,this))
        addSubcommand(EffectSubcommand(plugin,this))
    }

    override fun getDescription(): String {
        return "AtumCore base command"
    }

    override fun getUsage(): String {
        return "/atum"
    }

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
       executeHelp(sender,args)
    }
}