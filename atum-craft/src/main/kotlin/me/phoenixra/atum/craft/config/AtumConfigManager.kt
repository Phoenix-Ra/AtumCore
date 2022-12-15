package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigManager
import me.phoenixra.atum.core.config.LoadableConfig

class AtumConfigManager(private val plugin : AtumPlugin) :
    ConfigManager {

    private val configs = mutableMapOf<String, LoadableConfig>()

    override fun reloadAllConfigs() {
        for(conf in configs.values){
            conf.reload()
        }
    }

    override fun reloadConfig(name: String) {
        configs[name]?.reload()
    }

    override fun saveAllConfigs() {
        for(conf in configs.values){
            conf.save()
        }
    }

    override fun saveConfig(name: String) {
        configs[name]?.save()
    }

    override fun getConfig(name: String): LoadableConfig? {
        return configs[name]
    }

    override fun addConfig(config: LoadableConfig): ConfigManager {
        configs[config.name] = config
        return this
    }



    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}