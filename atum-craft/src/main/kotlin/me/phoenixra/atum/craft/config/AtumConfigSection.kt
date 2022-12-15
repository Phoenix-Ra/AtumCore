package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.config.ConfigType

class AtumConfigSection(
    type: ConfigType,
    values: Map<String, Any?> = emptyMap()
) : AtumConfig(type) {
    init {
        this.init(values)
    }
}