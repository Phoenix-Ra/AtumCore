package me.phoenixra.atum.plugin

import me.phoenixra.atum.core.AtumAPI
import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.command.AtumCommand
import me.phoenixra.atum.craft.AtumAPICraft
import me.phoenixra.atum.plugin.commands.AtumCoreCommand

class AtumSpigotPlugin : AtumPlugin() {

    init {
        instance = this
    }

    override fun handleEnable() {

    }

    override fun handleDisable() {

    }

    override fun loadPluginCommands(): MutableList<AtumCommand> {
        return mutableListOf(
            AtumCoreCommand(this)
        )
    }
    override fun loadAPI(): AtumAPI {
        return AtumAPICraft()
    }
    companion object{
        lateinit var instance : AtumSpigotPlugin
        private set
    }
}