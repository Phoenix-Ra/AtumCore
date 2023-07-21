package me.phoenixra.atum.craft.config

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.config.ConfigType
import me.phoenixra.atum.core.config.LoadableConfig
import me.phoenixra.atum.craft.config.utils.readToString
import me.phoenixra.atum.craft.config.utils.toMap
import java.io.*
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*


class AtumLoadableConfig(
    private val plugin: AtumPlugin,
    type: ConfigType,
    private val subDirectoryPath: String,
    confName: String,
    forceResourceLoad: Boolean
) : AtumConfig(type), LoadableConfig {

    private val file: File
    private var extraHeader: MutableList<String> = mutableListOf()

    constructor(plugin: AtumPlugin, file: File) :
            this(
                plugin,
                ConfigType.fromFile(file),
                file.path.replace(plugin.dataFolder.path,"").replace(file.name,""),
                file.nameWithoutExtension,
                false
            )
    init {
        val dir = File(this.plugin.dataFolder, subDirectoryPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file = File(dir, "$confName.${type.fileExtension}")
        if (!file.exists()) {
            createFile(forceResourceLoad)
        }
        reload()
        plugin.configManager.addConfig(this)
    }




    override fun createFile(forceResourceLoad: Boolean) {
        val inputStream = plugin.javaClass.getResourceAsStream(resourcePath)
        if(inputStream==null && forceResourceLoad) {
            throw NullPointerException("file not found inside the resources folder of a plugin")
        }

        if (!file.exists()) {
            file.createNewFile()
            val out: OutputStream = FileOutputStream(file)
            val headerWrite = StringBuilder()
            for (s in header) {
                headerWrite.append(s + "\n")
            }
            out.write(headerWrite.toString().toByteArray())
            inputStream?.readBytes()?.let { out.write(it) }
            out.close()
            inputStream?.close()
        }
    }

    override fun reload() {
        val reader = InputStreamReader(FileInputStream(file), Charsets.UTF_8)
        val s = reader.readToString()
        super.applyData(type.toMap(s))
    }

    override fun save() {
        if (file.delete()) {
            Files.write(
                file.toPath(),
                this.toPlaintext().toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
            )
        }
    }

    override fun getResourcePath(): String {
        val path: String =
            if (subDirectoryPath.isBlank()) fileName
            else subDirectoryPath + fileName

        return "/$path"
    }


    override fun toPlaintext(): String {
        val contents = StringBuilder()

        if (this.type == ConfigType.YAML) {
            for (s in header) {
                contents.append(s + "\n")
            }
        }

        for (line in super.toPlaintext().lines()) {
            if (line.startsWith("#")) {
                continue
            }

            contents.append(line + "\n")
        }

        return contents.toString()
    }

    override fun getHeader(): MutableList<String> {
        val list = mutableListOf(
            "## ---> Atum Development Team <--- ##",
            "# This Plugin uses Atum core :)",
            "# Our Discord: https://discord.gg/R2Wk5ZRXxp",
            "#####################################",
            "######## Plugin Authors: ########")
        list.addAll(plugin.description.authors.map { "#$it" })
        list.addAll(extraHeader)
        list.add("#####################################")
        list.add("############## ${Calendar.getInstance().get(Calendar.YEAR)} #################")
        list.add("\n")
        return list
    }

    override fun setExtraHeaderText(list: MutableList<String>?) {
        extraHeader = list ?: mutableListOf()
    }

    override fun getFile(): File {
        return file
    }
}