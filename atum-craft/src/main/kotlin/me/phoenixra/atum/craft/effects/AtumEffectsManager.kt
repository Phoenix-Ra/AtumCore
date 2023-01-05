package me.phoenixra.atum.craft.effects

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.effects.interfaces.Effect
import me.phoenixra.atum.core.effects.interfaces.EffectsManager
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class AtumEffectsManager(
    private val plugin: AtumPlugin
): EffectsManager {
    private val runningEffects: HashMap<Effect, BukkitTask> = hashMapOf()

    override fun startEffect(effect: Effect) {

        if (runningEffects.containsKey(effect)) {
            plugin.logger.info("EffectsManager: Tried to start an effect which is already active")
            return
        }

        val s = Bukkit.getScheduler()
        if (effect.period < 1) {
            plugin.logger.info("EffectsManager: Period can not be less than 1!")
            return
        }
        if (effect.delay < 0) {
            plugin.logger.info("EffectsManager: Delay can not be less than 0!")
            return
        }
        if (effect.iterations == 0) {
            plugin.logger.info("EffectsManager: Amount of iterations cannot be 0")
            return
        }
        if (effect.iterations < -1) {
            plugin.logger.info("EffectsManager: Amount of iterations cannot be less than -1")
            return
        }
        val task: BukkitTask = if (effect.iterations == 1 && effect.delay > 0) {
            if (effect.isAsync) s.runTaskLaterAsynchronously(
                plugin, effect::run, effect.delay
            )
            else s.runTaskLater(
                plugin, effect::run, effect.delay
            )

        } else {
            if (effect.isAsync) s.runTaskTimerAsynchronously(
                plugin, effect::run, effect.delay, effect.period
            )
            else s.runTaskTimer(
                plugin, effect::run, effect.delay, effect.period
            )
        }

        runningEffects[effect] = task
    }

    override fun finishEffect(effect: Effect) {
        val task = runningEffects[effect] ?: return
        task.cancel()
        runningEffects.remove(effect)
    }

    override fun cancelEffectsByID(id: String) {
        for(effect in runningEffects.keys){
            if(effect.id != id) continue
            effect.cancel(false)
        }
    }

    override fun cancelAllEffects() {
        for(effect in runningEffects.keys){
            effect.cancel(false)
        }
    }

    override fun getRunningEffectsByID(id: String): MutableList<Effect> {
        return runningEffects.keys.filter { it.id == id }.toMutableList()
    }

    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}