package com.phoenixra.atum.craft.event

import com.phoenixra.atum.core.AtumPlugin
import com.phoenixra.atum.core.events.EventManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class AtumEventManager(private val plugin : AtumPlugin) : EventManager {
    override fun registerListener(listener: Listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
    }

    override fun unregisterListener(listener: Listener) {
        TODO("Not yet implemented")
    }

    override fun unregisterAllListeners() {
        TODO("Not yet implemented")
    }

    override fun getPlugin(): AtumPlugin {
        return plugin;
    }
}