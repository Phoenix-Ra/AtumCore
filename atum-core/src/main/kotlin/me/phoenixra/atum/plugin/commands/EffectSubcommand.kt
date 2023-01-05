package me.phoenixra.atum.plugin.commands

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumSubcommand
import me.phoenixra.atum.core.command.CommandBase
import me.phoenixra.atum.core.effects.BaseEffectLocation
import me.phoenixra.atum.core.effects.BaseEffectVariable
import me.phoenixra.atum.core.effects.types.Helix
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EffectSubcommand(
    plugin: AtumPlugin,
    parent: CommandBase
) : AtumSubcommand(plugin, "effect", parent) {
    init {
        isConsoleAllowed = false;
        isPlayersAllowed = true;
    }
    override fun getDescription(): String {
        return "test some effects"
    }

    override fun getUsage(): String {
        return "/atum effect [type]"
    }

    override fun onTabComplete(sender: CommandSender, args: MutableList<String>): MutableList<String>? {
        return mutableListOf()
    }

    override fun onCommandExecute(sender: CommandSender, args: MutableList<String>) {
        val player = sender as Player
        when(args[0]){
            "helix" -> {
                val effect = Helix(plugin.effectsManager,BaseEffectLocation(player),
                    Particle.REDSTONE, 1, -1)
                effect.target = BaseEffectLocation(player).setOffsetXYZ(0.0,2.5,0.0)

                effect.particleColor = BaseEffectVariable(Color.GREEN)
                effect.thickness = BaseEffectVariable(0.06)
                effect.setRadius(0.8)
                effect.setParticleDrawPerTick(4)
                effect.setRadiusFunctionIncrementer(0.5)

                plugin.effectsManager.startEffect(effect)
            }
        }
    }

}