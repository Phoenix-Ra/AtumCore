package me.phoenixra.atum.craft.gui

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.gui.GuiController
import me.phoenixra.atum.core.gui.api.GuiFrame
import me.phoenixra.atum.core.gui.baseframes.ConfirmationFrame
import me.phoenixra.atum.core.gui.baseframes.WarningFrame
import me.phoenixra.atum.core.gui.events.FrameCloseEvent
import me.phoenixra.atum.core.gui.events.FrameComponentClickEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.server.PluginDisableEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AtumGuiController(
    private val plugin: AtumPlugin
): GuiController {

    private val registeredFrames = ConcurrentHashMap<UUID, GuiFrame>()

    @EventHandler(ignoreCancelled = true)
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin !== plugin) return
        unregisterAllFrames()
    }

    @EventHandler(ignoreCancelled = true)
    fun onClose(event: InventoryCloseEvent) {
        val entity = event.player as? Player ?: return
        val frame = registeredFrames[entity.uniqueId] ?: return
        try {
            frame.onClose()
            val frameCloseEvent = FrameCloseEvent(entity, frame)
            Bukkit.getPluginManager().callEvent(frameCloseEvent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        registeredFrames.remove(entity.uniqueId)
    }

    @EventHandler(ignoreCancelled = true)
    fun onInteract(event: InventoryClickEvent) {
        val entity = event.whoClicked as? Player ?: return
        val frame = registeredFrames[entity.uniqueId] ?: return
        event.isCancelled = true
        if (event.clickedInventory == null) return

        val component = frame.getComponent(event.slot, event.clickedInventory.type) ?: return
        val click = event.click
        var listener = component.getListener(click, event.clickedInventory!!.type) ?: return
        val player = entity
        val permission = component.getPermission(click, event.clickedInventory!!.type)
        if (permission != null) {
            if (!player.hasPermission(permission)) {
                if (guiDrawer == null) {
                    plugin.logger.warning(
                        String.format(
                            "PhoenixGuiController failed to load " +
                                    "WarningFrame for %s -> GuiDrawer is null!", player.name
                        )
                    )
                    return
                }
                guiDrawer.open(WarningFrame(guiDrawer, frame, player, "lack of permission"))
                return
            }
        }
        if (component.isConfirmationRequired(click, event.clickedInventory!!.type)) {
            if (guiDrawer == null) {
                plugin.logger.warning(
                    String.format(
                        "PhoenixGuiController failed to load " +
                                "ConfirmationFrame for %s -> GuiDrawer is null!", player.name
                    )
                )
                return
            }
            listener = Runnable {
                guiDrawer.open(
                    ConfirmationFrame(
                        guiDrawer, frame, frame.viewer, component.getListener(
                            click, event.clickedInventory!!
                                .type
                        )
                    )
                )
            }
        }
        val finalListener = listener
        Bukkit.getScheduler().runTask(plugin, Runnable {
            try {
                val currentItem = event.currentItem ?: return@runTask
                val frameComponentClickEvent = FrameComponentClickEvent(player, frame, component)
                Bukkit.getPluginManager().callEvent(frameComponentClickEvent)
                if (frameComponentClickEvent.isCancelled) {
                    return@runTask
                }
                finalListener.run()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                if (frame !is WarningFrame) {
                    guiDrawer.open(
                        WarningFrame(
                            guiDrawer, null, player,
                            "Unhandled error, contact with dev"
                        )
                    )
                } else player.closeInventory()
            }
        })
    }



    override fun registerFrame(frame: GuiFrame) {
        registeredFrames[frame.viewer.uniqueId] = frame
    }

    override fun unregisterAllFrames() {
        for(frame in registeredFrames.values){
            frame.viewer.closeInventory()
        }
    }

    override fun unregisterFrame(viewer: Player) {
        registeredFrames[viewer.uniqueId] ?: return
        viewer.closeInventory()
    }

    override fun getRegisteredFrames(): List<GuiFrame> {
        return registeredFrames.values.toList()
    }

    override fun getPlayerOpenedFrame(player: Player): GuiFrame? {
        return registeredFrames[player.uniqueId]
    }

    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}