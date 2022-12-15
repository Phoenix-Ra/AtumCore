package me.phoenixra.atum.craft.config.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.phoenixra.atum.core.config.Config
import java.lang.reflect.Type

object AtumGsonSerializer : JsonSerializer<Config> {
      val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .registerTypeAdapter(Config::class.java, this)
        .create()!!

    override fun serialize(src: Config, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return gson.toJsonTree(src.toMap())
    }
}