package me.phoenixra.atum.craft.event

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.events.EventManager
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class AtumEventManager(private val plugin : AtumPlugin) :
    EventManager {
    override fun registerListener(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
    }

    override fun unregisterListener(listener: Listener) {
        HandlerList.unregisterAll(listener)
    }

    override fun unregisterAllListeners() {
        HandlerList.unregisterAll(plugin)
    }

    override fun getPlugin(): AtumPlugin {
        return plugin;
    }
}