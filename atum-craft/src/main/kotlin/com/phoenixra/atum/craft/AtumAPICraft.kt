package com.phoenixra.atum.craft

import com.phoenixra.atum.core.AtumAPI
import com.phoenixra.atum.core.AtumPlugin
import com.phoenixra.atum.core.config.ConfigManager
import com.phoenixra.atum.core.config.ConfigType
import com.phoenixra.atum.core.config.LoadableConfig
import com.phoenixra.atum.core.events.EventManager
import com.phoenixra.atum.core.schedule.Scheduler
import com.phoenixra.atum.craft.event.AtumEventManager
import com.phoenixra.atum.craft.logger.AtumLogger
import com.phoenixra.atum.craft.shedule.AtumScheduler
import java.util.logging.Logger

class AtumAPICraft : AtumAPI {
    override fun createScheduler(plugin: AtumPlugin): Scheduler {
        return AtumScheduler(plugin)
    }

    override fun createEventManager(plugin: AtumPlugin): EventManager {
        return AtumEventManager(plugin)
    }

    override fun createConfigManager(plugin: AtumPlugin): ConfigManager {
        TODO("Not yet implemented")
    }

    override fun createLogger(plugin: AtumPlugin): Logger {
        return AtumLogger(plugin)
    }

    override fun createLoadableConfig(
        plugin: AtumPlugin,
        name: String,
        directory: String?,
        type: ConfigType
    ): LoadableConfig {
        TODO("Not yet implemented")
    }

    override fun createConfigFromResource(
        plugin: AtumPlugin,
        name: String,
        directory: String?,
        type: ConfigType
    ): LoadableConfig? {
        TODO("Not yet implemented")
    }

    override fun addPlugin(plugin: AtumPlugin) {
        TODO("Not yet implemented")
    }

    override fun removePlugin(plugin: AtumPlugin) {
        TODO("Not yet implemented")
    }
}