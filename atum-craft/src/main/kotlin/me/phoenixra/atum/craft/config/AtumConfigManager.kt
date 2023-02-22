package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigManager
import me.phoenixra.atum.core.config.LoadableConfig

class AtumConfigManager(private val plugin : AtumPlugin) :
    ConfigManager {

    private val configs = mutableMapOf<String, LoadableConfig>()

    override fun reloadAllConfigs() {
        val removal = mutableListOf<String>()
        for(entry in configs.entries){
            if(!entry.value.file.exists()||!entry.value.file.isFile){
                removal.add(entry.key)
                continue
            }
            entry.value.reload()
        }
        removal.forEach { configs.remove(it) }
    }

    override fun reloadConfig(name: String) {
        configs[name]?.reload()
    }

    override fun saveAllConfigs() {
        val removal = mutableListOf<String>()
        for(entry in configs.entries){
            if(!entry.value.file.exists()||!entry.value.file.isFile){
                removal.add(entry.key)
                continue
            }
            entry.value.save()
        }
        removal.forEach { configs.remove(it) }
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