package me.phoenixra.atum.craft.config.utils

import me.phoenixra.atum.craft.config.AtumConfigSection
import me.phoenixra.atum.core.config.ConfigType
import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import org.bukkit.configuration.file.YamlConstructor
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.BufferedReader
import java.io.Reader

fun ConfigType.toMap(input: String?): Map<String, Any?> =
    this.handler.toMap(input)

fun ConfigType.toString(map: Map<String, Any?>): String =
    this.handler.toString(map)

internal fun Any?.constrainConfigTypes(type: ConfigType): Any? = when (this) {
    is Map<*, *> -> AtumConfigSection(type, this.normalizeToConfig(type))
    is Iterable<*> -> {
        when(this.firstOrNull()){
            null -> {
                mutableListOf<Any>()
            }
            is Map<*,*> -> {
                this as Iterable<Map<*, *>>
                this.map { map -> AtumConfigSection(type, map.normalizeToConfig(type)) }
            }
            else -> this.toMutableList()
        }
    }
    else -> this
}

internal fun Map<*, *>.normalizeToConfig(type: ConfigType): Map<String, Any?> {
    val building = mutableMapOf<String, Any?>()

    for ((unprocessedKey, value) in this.entries) {
        if (unprocessedKey == null || value == null) {
            continue
        }

        val key = unprocessedKey.toString()

        val constrained = value.constrainConfigTypes(type)

        building[key] = constrained
    }

    return building
}

fun Reader.readToString(): String {
    val input = this as? BufferedReader ?: BufferedReader(this)
    val builder = StringBuilder()

    var line: String?
    input.use {
        while (it.readLine().also { read -> line = read } != null) {
            builder.append(line)
            builder.append('\n')
        }
    }

    return builder.toString()
}

private val ConfigType.handler: ConfigTypeHandler
    get() = when (this) {
        ConfigType.JSON -> JSONConfigTypeHandler
        ConfigType.YAML -> YamlConfigTypeHandler
        ConfigType.TOML -> TOMLConfigTypeHandler
    }

private abstract class ConfigTypeHandler(
    val type: ConfigType
) {
    fun toMap(input: String?): Map<String, Any?> {
        if (input == null || input.isBlank()) {
            return emptyMap()
        }

        return parseToMap(input).normalizeToConfig(type)
    }

    protected abstract fun parseToMap(input: String): Map<*, *>

    abstract fun toString(map: Map<String, Any?>): String
}

private object YamlConfigTypeHandler : ConfigTypeHandler(ConfigType.YAML) {
    private fun newYaml(): Yaml {
        val yamlOptions = DumperOptions()
        val loaderOptions = LoaderOptions()
        val representer = AtumRepresenter()

        loaderOptions.maxAliasesForCollections = Int.MAX_VALUE
        loaderOptions.isAllowDuplicateKeys = false
        yamlOptions.indent = 2
        yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

        representer.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

        return Yaml(
            YamlConstructor(),
            representer,
            yamlOptions,
            loaderOptions,
        )
    }

    override fun parseToMap(input: String): Map<*, *> {
        return newYaml().load(input) ?: emptyMap<Any, Any>()
    }

    override fun toString(map: Map<String, Any?>): String {
        return newYaml().dump(map)
    }
}

private object JSONConfigTypeHandler : ConfigTypeHandler(ConfigType.JSON) {
    override fun parseToMap(input: String): Map<*, *> {
        return AtumGsonSerializer.gson.fromJson(input, Map::class.java)
    }

    override fun toString(map: Map<String, Any?>): String {
        return AtumGsonSerializer.gson.toJson(map)
    }
}

private object TOMLConfigTypeHandler : ConfigTypeHandler(ConfigType.TOML) {
    override fun parseToMap(input: String): Map<*, *> {
        return Toml().read(input).toMap()
    }

    override fun toString(map: Map<String, Any?>): String {
        val writer = TomlWriter.Builder()
            .build()

        return writer.write(map)
    }
}