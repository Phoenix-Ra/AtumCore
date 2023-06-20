package me.phoenixra.atum.craft.event

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.events.EventManager
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class AtumEventManager(private val plugin : AtumPlugin) :
    EventManager {
    private val pluginManager = Bukkit.getPluginManager()
    private val registry: Set<Listener> = HashSet()
    override fun callEvent(event: Event) {
        pluginManager.callEvent(event)
    }

    override fun registerListener(listener: Listener) {
        pluginManager.registerEvents(listener, plugin)
    }

    override fun unregisterListener(listener: Listener) {
        HandlerList.unregisterAll(listener)
    }

    override fun unregisterAllListeners() {
        HandlerList.unregisterAll(plugin)
    }

    override fun getRegisteredListeners(): Set<Listener> {
        return registry;
    }

    override fun getPlugin(): AtumPlugin {
        return plugin;
    }
}