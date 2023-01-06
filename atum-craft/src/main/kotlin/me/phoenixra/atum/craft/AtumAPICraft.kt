package me.phoenixra.atum.craft

import me.phoenixra.atum.core.AtumAPI
import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigManager
import me.phoenixra.atum.core.config.ConfigType
import me.phoenixra.atum.core.config.LoadableConfig
import me.phoenixra.atum.core.effects.interfaces.EffectsManager
import me.phoenixra.atum.core.events.EventManager
import me.phoenixra.atum.core.gui.GuiController
import me.phoenixra.atum.core.schedule.Scheduler
import me.phoenixra.atum.craft.config.AtumConfigManager
import me.phoenixra.atum.craft.config.AtumLoadableConfig
import me.phoenixra.atum.craft.effects.AtumEffectsManager
import me.phoenixra.atum.craft.event.AtumEventManager
import me.phoenixra.atum.craft.gui.AtumGuiController
import me.phoenixra.atum.craft.logger.AtumLogger
import me.phoenixra.atum.craft.shedule.AtumScheduler
import java.io.File
import java.util.logging.Logger

class AtumAPICraft : AtumAPI {
    private val loadedPlugins = mutableMapOf<String, AtumPlugin>()

    override fun createScheduler(plugin: AtumPlugin): Scheduler {
        return AtumScheduler(plugin)
    }

    override fun createEventManager(plugin: AtumPlugin): EventManager {
        return AtumEventManager(plugin)
    }

    override fun createConfigManager(plugin: AtumPlugin): ConfigManager {
        return AtumConfigManager(plugin)
    }

    override fun createGuiController(plugin: AtumPlugin): GuiController {
        return AtumGuiController(plugin)
    }

    override fun createEffectsManager(plugin: AtumPlugin): EffectsManager {
        return AtumEffectsManager(plugin)
    }

    override fun createLogger(plugin: AtumPlugin): Logger {
        return AtumLogger(plugin)
    }

    override fun loadConfiguration(plugin: AtumPlugin, file: File): LoadableConfig {
        return AtumLoadableConfig(plugin, file)
    }

    override fun createLoadableConfig(
        plugin: AtumPlugin,
        name: String,
        directory: String,
        type: ConfigType,
        forceLoadResource: Boolean
    ): LoadableConfig {
        return AtumLoadableConfig(plugin, type, directory, name,forceLoadResource)
    }

    override fun getPluginByName(name: String): AtumPlugin? {
        return loadedPlugins[name.lowercase()]
    }

    override fun getLoadedPlugins(name: String): MutableList<AtumPlugin> {
        return loadedPlugins.values.toMutableList()
    }

    override fun addPlugin(plugin: AtumPlugin) {
        loadedPlugins[plugin.name.lowercase()] = plugin
    }

    override fun removePlugin(plugin: AtumPlugin) {
        loadedPlugins.remove(plugin.name.lowercase())
    }
}