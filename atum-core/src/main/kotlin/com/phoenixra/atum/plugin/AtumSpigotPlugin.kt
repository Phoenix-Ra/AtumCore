package com.phoenixra.atum.plugin

import com.phoenixra.atum.core.AtumAPI
import com.phoenixra.atum.core.AtumPlugin
import org.bukkit.event.Listener

class AtumSpigotPlugin : AtumPlugin() {

    init {
        instance = this
    }

    override fun handleEnable() {
        TODO("Not yet implemented")
    }

    override fun handleDisable() {
        TODO("Not yet implemented")
    }

    override fun loadListeners(): MutableList<Listener> {
        TODO("Not yet implemented")
    }

    override fun loadAPI(): AtumAPI {
        return AtumAPImpl();
    }
    companion object{
        lateinit var instance : AtumSpigotPlugin
        private set
    }
}