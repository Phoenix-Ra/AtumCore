package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigManager
import me.phoenixra.atum.core.config.LoadableConfig
import me.phoenixra.atum.core.config.category.ConfigCategory

class AtumConfigManager(private val plugin: AtumPlugin) :
    ConfigManager {

    private val configs = mutableMapOf<String, LoadableConfig>()
    private val configCategoryRegistry = HashMap<String, ConfigCategory>()

    override fun reloadAllConfigs() {
        val removal = mutableListOf<String>()
        for (entry in configs.entries) {
            if (!entry.value.file.exists() || !entry.value.file.isFile) {
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
        for (entry in configs.entries) {
            if (!entry.value.file.exists() || !entry.value.file.isFile) {
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

    override fun reloadAllConfigCategories() {
        for (entry in configCategoryRegistry.values) {
            try {
                entry.reload()
            } catch (exception: Exception) {
                plugin.logger.info(
                    """
                    &eException while trying to reload the config category with id: ${entry.id}
                    Message: ${exception.message}
                    """.trimIndent()
                )
            }
        }
    }

    override fun reloadConfigCategory(id: String) {
        try {
            val config = configCategoryRegistry[id] ?: return
            config.reload()
        } catch (exception: Exception) {
            plugin.logger.info(
                """
            &eException while trying to reload the config category with id: $id
            Message: ${exception.message}
            """.trimIndent()
            )
        }
    }

    override fun getConfigCategory(id: String): ConfigCategory? {
        return configCategoryRegistry[id]
    }

    override fun addConfigCategory(configCategory: ConfigCategory) {
        configCategoryRegistry[configCategory.id] = configCategory
    }


    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}