package me.phoenixra.atum.craft.gui

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.gui.GuiController
import me.phoenixra.atum.core.gui.GuiDrawer
import me.phoenixra.atum.core.gui.api.GuiFrame
import me.phoenixra.atum.core.gui.baseframes.ConfirmationFrame
import me.phoenixra.atum.core.gui.baseframes.WarningFrame
import me.phoenixra.atum.core.gui.events.GuiFrameCloseEvent
import me.phoenixra.atum.core.gui.events.GuiFrameClickEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AtumGuiController(
    private val plugin: AtumPlugin
) : GuiController, Runnable {

    private val registeredFrames = ConcurrentHashMap<UUID, GuiFrame>()
    private val guiDrawer = AtumGuiDrawer(plugin, this)

    private var updaterEnabled = false
    private var task: BukkitTask? = null


    override fun run() {
        for(frame in registeredFrames.values){
            frame.updateComponents()
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin !== plugin) return
        unregisterAllFrames()
    }

    @EventHandler(priority = EventPriority.HIGH)
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

    @EventHandler(priority = EventPriority.HIGH)
    fun onInteract(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val clickedInventory = event.clickedInventory
        val frame = registeredFrames[player.uniqueId] ?: return

        if(!frame.isInventoryInteractive) {
            event.isCancelled = true
            event.cursor = null
        }

        //frame click event
        val component = frame.getComponent(event.slot, clickedInventory?.type)
        val frameComponentClickEvent = GuiFrameClickEvent(
            player,
            frame,
            component,
            event
        )
        Bukkit.getPluginManager().callEvent(frameComponentClickEvent)
        if (frameComponentClickEvent.isCancelled) return

        if(component == null) return

        val click = event.click
        var listener = component.getListener(click)
        val permission = component.getPermission(click)
        if (permission != null && !player.hasPermission(permission)) {
            guiDrawer.open(WarningFrame(guiDrawer, frame, player, "lack of permission"))
            return
        }
        if (listener!=null && component.isConfirmationRequired(click)) {
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
                finalListener?.run()
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

    override fun enableUpdater(value: Boolean) {
        updaterEnabled = value
        val localTask = task
        if(updaterEnabled) {
            if(localTask == null || localTask.isCancelled){
                task = plugin.scheduler.runTimer(0, 1, this)
            }
        }else{
            if(localTask != null && !localTask.isCancelled){
                localTask.cancel()
            }
            task = null
        }
    }

    override fun isUpdaterEnabled(): Boolean {
        return updaterEnabled
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