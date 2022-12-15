package me.phoenixra.atum.craft

import me.phoenixra.atum.core.AtumAPI
import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigManager
import me.phoenixra.atum.core.config.ConfigType
import me.phoenixra.atum.core.config.LoadableConfig
import me.phoenixra.atum.core.events.EventManager
import me.phoenixra.atum.core.schedule.Scheduler
import me.phoenixra.atum.craft.config.AtumConfigManager
import me.phoenixra.atum.craft.config.AtumLoadableConfig
import me.phoenixra.atum.craft.event.AtumEventManager
import me.phoenixra.atum.craft.logger.AtumLogger
import me.phoenixra.atum.craft.shedule.AtumScheduler
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

    override fun createLogger(plugin: AtumPlugin): Logger {
        return AtumLogger(plugin)
    }

    override fun createLoadableConfig(
        plugin: AtumPlugin,
        name: String,
        directory: String,
        type: ConfigType
    ): LoadableConfig {
        return AtumLoadableConfig(type, plugin, directory, name)
    }

    override fun getPluginByName(name: String): AtumPlugin? {
        return loadedPlugins[name]
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