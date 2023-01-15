package me.phoenixra.atum.craft.logger

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.utils.formatAtum
import org.bukkit.Bukkit
import java.util.logging.Level
import java.util.logging.Logger

class AtumLogger(private val plugin : AtumPlugin)
    : Logger(plugin.name, null)  {
    init {
        parent = plugin.server.logger
        this.level = Level.ALL
    }


    //to allow colorizing
    override fun info(message: String) {
        Bukkit.getConsoleSender().sendMessage("[${plugin.name}] $message".formatAtum())
    }
}