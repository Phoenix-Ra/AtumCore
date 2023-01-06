package me.phoenixra.atum.craft.gui

import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors
import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.exceptions.AtumException
import me.phoenixra.atum.core.gui.GuiDrawer
import me.phoenixra.atum.core.gui.api.GuiComponent
import me.phoenixra.atum.core.gui.api.GuiFrame
import me.phoenixra.atum.core.gui.baseframes.WarningFrame
import me.phoenixra.atum.core.gui.events.GuiFrameOpenEvent
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

class AtumGuiDrawer(
    private val plugin: AtumPlugin,
    private val guiController: AtumGuiController
    ) : GuiDrawer {
    private val OPENING = ConcurrentHashMap<UUID, GuiFrame>()

    @Getter @Setter @Accessors(chain = true)
    private val debug = false

    override fun open(frame: GuiFrame) {
        val uuid = frame.viewer.uniqueId
        if (frame == OPENING[uuid]) return

        OPENING[uuid] = frame
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val viewer = frame.viewer
            try {
                val inventory: Inventory = prepareInventory(frame)
                if (frame != OPENING[uuid]) return@Runnable
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    val event = GuiFrameOpenEvent(viewer, frame)
                    Bukkit.getPluginManager().callEvent(event)
                    if (event.isCancelled) return@Runnable
                    viewer.openInventory(inventory)
                    guiController.registerFrame(frame)
                    OPENING.remove(uuid)
                })
            } catch (ex: AtumException) {
                ex.printStackTrace()
                if (frame !is WarningFrame) {
                    open(WarningFrame(this, null, viewer, ex.messageToPlayer))
                } else viewer.closeInventory()
            } catch (e: Exception) {
                e.printStackTrace()
                if (frame !is WarningFrame) {
                    open(
                        WarningFrame(
                            this,
                            null,
                            viewer,
                            "&8&oUnhandled error,\n&8&o please contact with server administration"
                        )
                    )
                } else viewer.closeInventory()
            }
        })
    }

    override fun update(frame: GuiFrame, async: Boolean) {
        val task = Runnable {
            val viewer = frame.viewer

            try {
                setComponents(viewer.openInventory.topInventory, frame)

            } catch (ex: AtumException) {
                ex.printStackTrace()
                if (frame !is WarningFrame) {
                    open(WarningFrame(this, null, viewer, ex.messageToPlayer))
                } else viewer.closeInventory()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                if (frame !is WarningFrame) {
                    open(
                        WarningFrame(
                            this,
                            null,
                            viewer,
                            "&8&oUnhandled error,\n&8&o please contact with server administration"
                        )
                    )
                } else viewer.closeInventory()
            }
        }
        if(async){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
        }else {
            Bukkit.getScheduler().runTask(plugin,task)
        }
    }


    @Throws(AtumException::class)
    private fun prepareInventory(frame: GuiFrame): Inventory {
        val inventory = Bukkit.createInventory(frame.viewer, frame.size, frame.title)
        val start = System.currentTimeMillis()
        setComponents(inventory, frame)
        if (debug) {
            plugin.logger.log(
                Level.INFO,
                    "It took ${System.currentTimeMillis() - start} " +
                            "millisecond(s) to load the frame" +
                            " ${frame.title} for ${frame.viewer.name}"

            )
        }
        return inventory
    }

    @Throws(AtumException::class)
    private fun setComponents(inventory: Inventory, frame: GuiFrame) {
        frame.clear()
        frame.createComponents()
        inventory.clear()
        val components = frame.components
        if (components.isEmpty()) {
            plugin.logger.warning("Frame ${frame.title} has no components")
            return
        }
        for (c in frame.components) {
            if (c.inventoryType == InventoryType.PLAYER) {
                if (c.slot >= frame.viewer.inventory.size) continue
                checkLorePermission(frame, c)
                frame.viewer.inventory.setItem(c.slot, c.item)
            } else {
                if (c.slot >= frame.size) continue
                checkLorePermission(frame, c)
                inventory.setItem(c.slot, c.item)
            }
        }
    }

    private fun checkLorePermission(frame: GuiFrame, component: GuiComponent) {
        val itemMeta = component.itemMeta
        if (itemMeta == null || itemMeta.lore == null || component.lorePermission == null) return
        val lore = itemMeta.lore
        if (!frame.viewer.hasPermission(component.lorePermission!!)) {
            lore!!.clear()
            lore.add("&4You don't have the required permission")
            itemMeta.lore = lore
            component.setItemMeta(itemMeta)
        }
    }


    override fun getPlugin(): AtumPlugin {
        return plugin
    }
}