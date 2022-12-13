package com.phoenixra.atum.plugin

import com.phoenixra.atum.core.AtumAPI
import com.phoenixra.atum.core.AtumPlugin
import com.phoenixra.atum.core.config.ConfigManager
import com.phoenixra.atum.core.config.ConfigType
import com.phoenixra.atum.core.config.LoadableConfig
import com.phoenixra.atum.core.events.EventManager
import com.phoenixra.atum.core.schedule.Scheduler
import java.util.logging.Logger

class AtumAPImpl : AtumAPI {
    override fun createScheduler(plugin: AtumPlugin): Scheduler {
        TODO("Not yet implemented")
    }

    override fun createEventManager(plugin: AtumPlugin): EventManager {
        TODO("Not yet implemented")
    }

    override fun createConfigManager(plugin: AtumPlugin): ConfigManager {
        TODO("Not yet implemented")
    }

    override fun createLogger(plugin: AtumPlugin): Logger {
        TODO("Not yet implemented")
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