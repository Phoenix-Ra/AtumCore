package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumSubcommand
import me.phoenixra.atum.core.command.CommandBase
import me.phoenixra.atum.core.command.notifyNull
import me.phoenixra.atum.core.effects.BaseEffectLocation
import me.phoenixra.atum.core.effects.BaseEffectVariable
import me.phoenixra.atum.core.effects.types.DynamicCircle
import me.phoenixra.atum.core.effects.types.Helix
import me.phoenixra.atum.core.nametag.NameTagManager
import me.phoenixra.atum.core.utils.formatAtum
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class NameTagSubcommand(
    plugin: AtumPlugin,
    parent: CommandBase
) : AtumSubcommand(plugin, "nametag", parent) {
    init {
        isConsoleAllowed = true;
        isPlayersAllowed = true;
    }
    override fun getDescription(): String {
        return "test nametags"
    }

    override fun getUsage(): String {
        return "/atum nametag setPrefix [player] [prefix]"
    }

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String>? {
        return mutableListOf()
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        val player = sender as Player
        when(args[0].lowercase()){
            "setprefix" -> {
                notifyFalse(args.size == 3,"/atum nametag setPrefix [player] [prefix]",false)

                val target: Player = notifyNull(plugin.server.getPlayer(args[1]), "Player not found",false)
                val prefix = args[2]
                NameTagManager.setPrefix(target,prefix.formatAtum())
                notify("Set prefix of ${target.name} to $prefix  SUCCESS",false)
            }
            "setprefix1" -> {
                notifyFalse(args.size == 4,"/atum nametag setPrefix1 [target] [source] [prefix]",false)

                val target: Player = notifyNull(plugin.server.getPlayer(args[1]), "Target not found",false)
                val source: Player = notifyNull(plugin.server.getPlayer(args[2]), "Source not found",false)
                val prefix = args[3]
                NameTagManager.setPrefix(target, source, prefix.formatAtum())
                notify("Set prefix of ${target.name} to $prefix for the ${source.name}  SUCCESS",false)
            }
            "setcolor" -> {
                notifyFalse(args.size == 4,"/atum nametag setColor [target] [source] [color]",false)

                val target: Player = notifyNull(plugin.server.getPlayer(args[1]), "Target not found",false)
                val source: Player = notifyNull(plugin.server.getPlayer(args[2]), "Source not found",false)
                val color = args[3]
                NameTagManager.setColor(target, source, color.first())
                notify("Set color of ${target.name} to $color for the ${source.name}  SUCCESS",false)
            }
        }
    }
}