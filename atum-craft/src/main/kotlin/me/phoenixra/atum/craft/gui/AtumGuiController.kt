package me.phoenixra.atum.craft.gui

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.gui.GuiController
import me.phoenixra.atum.core.gui.GuiDrawer
import me.phoenixra.atum.core.gui.api.GuiFrame
import me.phoenixra.atum.core.gui.baseframes.ConfirmationFrame
import me.phoenixra.atum.core.gui.baseframes.WarningFrame
import me.phoenixra.atum.core.gui.events.GuiFrameCloseEvent
import me.phoenixra.atum.core.gui.events.GuiComponentClickEvent
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
) : GuiController {

    private val registeredFrames = ConcurrentHashMap<UUID, GuiFrame>()
    private val guiDrawer = AtumGuiDrawer(plugin, this)

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
            val frameCloseEvent = GuiFrameCloseEvent(entity, frame)
            Bukkit.getPluginManager().callEvent(frameCloseEvent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        registeredFrames.remove(entity.uniqueId)
    }

    @EventHandler(ignoreCancelled = true)
    fun onInteract(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val frame = registeredFrames[player.uniqueId] ?: return
        val clickedInventory = event.clickedInventory
        event.isCancelled = true
        if (clickedInventory == null) return

        val component = frame.getComponent(event.slot, clickedInventory.type) ?: return
        val click = event.click
        var listener = component.getListener(click) ?: return
        val permission = component.getPermission(click)
        if (permission != null && !player.hasPermission(permission)) {
            guiDrawer.open(WarningFrame(guiDrawer, frame, player, "lack of permission"))
            return
        }
        if (component.isConfirmationRequired(click)) {
            listener = Runnable {
                guiDrawer.open(ConfirmationFrame(
                        guiDrawer,
                        frame,
                        frame.viewer,
                        component.getListener(click)
                    ))
            }
        }
        val finalListener = listener
        Bukkit.getScheduler().runTask(plugin, Runnable {
            try {
                event.currentItem ?: return@Runnable
                val frameComponentClickEvent = GuiComponentClickEvent(
                        player,
                        frame,
                        component
                    )
                Bukkit.getPluginManager().callEvent(frameComponentClickEvent)

                if (frameComponentClickEvent.isCancelled) return@Runnable

                finalListener.run()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                if (frame !is WarningFrame) {
                    guiDrawer.open(
                        WarningFrame(
                            guiDrawer, null, player,
                            "&8&oUnhandled error,\n" +
                                    "&8&o please contact with server administration"
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
        for (frame in registeredFrames.values) {
            frame.viewer.closeInventory()
        }
        System.gc()
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

    override fun getGuiDrawer(): GuiDrawer {
        return guiDrawer
    }

    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}