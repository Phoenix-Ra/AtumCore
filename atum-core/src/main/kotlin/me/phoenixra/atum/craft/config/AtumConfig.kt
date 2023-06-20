package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.AtumAPI
import me.phoenixra.atum.core.config.Config
import me.phoenixra.atum.core.config.ConfigType
import me.phoenixra.atum.core.placeholders.InjectablePlaceholder
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext
import me.phoenixra.atum.craft.config.utils.constrainConfigTypes
import me.phoenixra.atum.craft.config.utils.normalizeToConfig
import me.phoenixra.atum.craft.config.utils.toString
import org.bukkit.configuration.file.YamlConfiguration
import java.util.concurrent.ConcurrentHashMap


open class AtumConfig(
     private val configType : ConfigType
 ) : Config {
    var injections = mutableListOf<InjectablePlaceholder>()
    private val values = ConcurrentHashMap<String, Any?>()

    fun init(values: Map<String, Any?>) {
        this.values.clear()
        this.values.putAll(values.normalizeToConfig(this.configType))
    }

    override fun addInjectablePlaceholder(placeholders: MutableIterable<InjectablePlaceholder>) {
        injections.addAll(placeholders);
    }

    override fun removeInjectablePlaceholder(placeholders: MutableIterable<InjectablePlaceholder>) {
        injections.removeAll(placeholders);
    }

    override fun clearInjectedPlaceholders() {
        injections.clear();
    }

    override fun getPlaceholderInjections(): MutableList<InjectablePlaceholder> {
        return injections;
    }

    override fun toPlaintext(): String {
        return configType.toString(this.values)
    }

    override fun hasPath(path: String): Boolean {
        return get(path) != null
    }

    override fun getKeys(deep: Boolean): List<String> {
        return if(deep){
            return recurseKeys(mutableSetOf(),"")
        }else values.keys.toList()
    }
    override fun recurseKeys(current: MutableSet<String>, root: String): List<String> {
        val list = mutableSetOf<String>()
        for (key in getKeys(false)) {
            list.add("$root$key")
            val found = get(key)

            if (found is Config) {
                list.addAll(found.recurseKeys(current, "$root$key."))
            }

        }

        return list.toList()
    }
    override fun get(path: String): Any? {
        val nearestPath = path.split(".")[0]

        if (path.contains(".")) {
            val remainingPath = path.removePrefix("${nearestPath}.")

            if (remainingPath.isEmpty()) {
                return null
            }

            val first = get(nearestPath)

            return if (first is Config) {
                first.get(remainingPath)
            } else null

        }

        return values[nearestPath]
    }

    override fun set(path: String, obj: Any?) {
        val nearestPath = path.split(".")[0]

        if (path.contains(".")) {
            val remainingPath = path.removePrefix("${nearestPath}.")

            if (remainingPath.isEmpty()) {
                return
            }

            var section = getSubsection(nearestPath) // Creates a section if null, therefore it can be set.
            if(section==null){
                section = AtumConfigSection(type)
            }
            section.set(remainingPath, obj)
            values[nearestPath] = section // Set the value
            return
        }

        if (obj == null) {
            values.remove(nearestPath)
        } else {
            values[nearestPath] = obj.constrainConfigTypes(type)
        }
    }

    override fun getIntOrNull(path: String): Int? {
        return (get(path) as? Number)?.toInt()
    }

    override fun getIntListOrNull(path: String): List<Int>? {
        return getList<Number>(path)?.map { it.toInt() }?.toList()
    }

    override fun getBoolOrNull(path: String): Boolean? {
        return get(path) as? Boolean
    }

    override fun getBoolListOrNull(path: String): List<Boolean>? {
        return getList<Boolean>(path)?.toList()
    }

    override fun getStringOrNull(path: String): String? {
        return get(path)?.toString()
    }

    override fun getStringListOrNull(path: String): List<String>? {
        return getList<Any>(path)?.map { it.toString() }?.toList()
    }

    override fun getDoubleOrNull(path: String): Double? {
        return (get(path) as? Number)?.toDouble()
    }

    override fun getDoubleListOrNull(path: String): List<Double>? {
        return getList<Number>(path)?.map { it.toDouble() }
    }

    override fun getSubsection(path: String): Config? {
        return get(path) as? Config
    }

    override fun getSubsectionListOrNull(path: String): List<Config>? {
        val asIterable = get(path) as? Iterable<*> ?: return null
        val asList = asIterable.toMutableList()
        asList.removeIf {
            it !is Config
        }

        return asList as List<Config>
    }

    override fun getEvaluated(path: String, context: PlaceholderContext): Double {
        val text = getStringOrNull(path) ?: return 0.0

        val context1 = context.withInjectableContext(this)
        return AtumAPI.getInstance().evaluate(text, context1)
    }

    override fun getType(): ConfigType {
        return configType
    }

    private inline fun <reified T> getList(path: String): List<T>? {
        val asIterable = get(path) as? Iterable<*> ?: return null
        val asList = asIterable.toList()

        if (asList.firstOrNull() !is T?) {
            return emptyList()
        }

        return asList as List<T>
    }



    override fun toMap(): MutableMap<String, Any?> {
        return values.toMutableMap()
    }
    override fun toBukkit(): YamlConfiguration {
        val temp = YamlConfiguration()
        temp.createSection("temp", this.values.toMap())
        val section = temp.getConfigurationSection("temp")!!

        val bukkit = YamlConfiguration()
        for (key in section.getKeys(true)) {
            bukkit.set(key, section.get(key))
        }
        return bukkit
    }
}