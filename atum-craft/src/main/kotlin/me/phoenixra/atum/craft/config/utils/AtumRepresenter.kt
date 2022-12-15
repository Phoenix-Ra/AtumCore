package me.phoenixra.atum.craft.config.utils

import me.phoenixra.atum.core.config.Config
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer

class AtumRepresenter : Representer(){
    init {
        multiRepresenters[Config::class.java] = RepresentConfig(multiRepresenters[Map::class.java]!!)
    }

    private class RepresentConfig(
        val handle: Represent
    ) : Represent {
        override fun representData(data: Any): Node {
            data as Config
            return handle.representData(data.toMap())
        }
    }

}