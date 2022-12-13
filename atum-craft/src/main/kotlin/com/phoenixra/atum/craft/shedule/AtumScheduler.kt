package com.phoenixra.atum.craft.shedule

import com.phoenixra.atum.core.AtumPlugin
import com.phoenixra.atum.core.schedule.Scheduler
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class AtumScheduler(private val plugin : AtumPlugin) : Scheduler{
    override fun run(runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTask(plugin, runnable)
    }

    override fun runAsync(runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
    }

    override fun runLater(
        ticksLater: Long,
        runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, ticksLater)
    }


    override fun runTimer(
        delay: Long,
        repeat: Long,
        runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, repeat)
    }

    override fun runTimerAsync(
        delay: Long,
        repeat: Long,
        runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, repeat)
    }
    override fun cancelAll() {
        Bukkit.getScheduler().cancelTasks(plugin)
    }

    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}