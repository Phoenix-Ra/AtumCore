package com.phoenixra.atum.craft.config

import com.phoenixra.atum.core.AtumPlugin
import com.phoenixra.atum.core.config.ConfigManager
import com.phoenixra.atum.core.config.LoadableConfig

class AtumConfigManager(private val plugin : AtumPlugin) : ConfigManager {

    private val configs = mutableMapOf<String, LoadableConfig>()

    override fun loadAllConfigs() {
        TODO("Not yet implemented")
    }

    override fun loadConfig(name: String) {
        TODO("Not yet implemented")
    }

    override fun reloadAllConfigs() {
        TODO("Not yet implemented")
    }

    override fun reloadConfig(name: String) {
        TODO("Not yet implemented")
    }

    override fun saveAllConfigs() {
        for(conf in configs.values){
            conf.save()
        }
    }

    override fun saveConfig(name: String) {
        if(!configs.containsKey(name)) return
        configs[name]!!.save()
    }

    override fun addConfig(config: LoadableConfig): ConfigManager {
        configs[config.name] = config
        return this
    }



    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}