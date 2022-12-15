package me.phoenixra.atum.plugin

import me.phoenixra.atum.core.AtumAPI
import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.craft.AtumAPICraft
import org.bukkit.event.Listener

class AtumSpigotPlugin : AtumPlugin() {

    init {
        instance = this
    }

    override fun handleEnable() {

    }

    override fun handleDisable() {

    }

    override fun loadAPI(): AtumAPI {
        return AtumAPICraft()
    }
    companion object{
        lateinit var instance : AtumSpigotPlugin
        private set
    }
}