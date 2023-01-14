package me.phoenixra.atum.core.gui

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.gui.api.GuiFrame

fun GuiFrame.open(plugin: AtumPlugin, async: Boolean){
    plugin.guiController.guiDrawer.open(this,async)
}
fun GuiFrame.open(plugin: AtumPlugin){
    plugin.guiController.guiDrawer.open(this)
}

fun GuiFrame.update(plugin: AtumPlugin, async: Boolean){
    plugin.guiController.guiDrawer.update(this,async)
}
fun GuiFrame.update(plugin: AtumPlugin){
    plugin.guiController.guiDrawer.update(this)
}