package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumSubcommand
import me.phoenixra.atum.core.command.CommandBase
import me.phoenixra.atum.plugin.AtumSpigotPlugin
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class ReloadSubcommand(
    plugin: AtumPlugin,
    parent: CommandBase
) : AtumSubcommand(plugin, "reload", parent) {

    override fun getDescription(): String {
        return "Reload the plugin"
    }

    override fun getUsage(): String {
        return "/atum reload"
    }

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        AtumSpigotPlugin.instance.reload()
        sender.sendMessage("${ChatColor.GREEN}Successfully reloaded")
    }

}