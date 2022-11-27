package me.phoenixra.core.gui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.core.gui.api.FrameCloseEvent;
import me.phoenixra.core.gui.api.FrameComponentClickEvent;
import me.phoenixra.core.gui.api.PhoenixFrame;
import me.phoenixra.core.gui.api.PhoenixFrameComponent;
import me.phoenixra.core.gui.baseframes.ConfirmationFrame;
import me.phoenixra.core.gui.baseframes.WarningFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoenixGuiController implements Listener {
    private final Plugin plugin;
    private final Map<UUID, PhoenixFrame> frames;
    @Getter
    @Setter
    @Accessors(chain = true)
    private GuiDrawer guiDrawer;

    public PhoenixGuiController(Plugin plugin) {
        this.plugin = plugin;
        this.frames = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if (!(entity instanceof Player)) {
            return;
        }
        if(!frames.containsKey(entity.getUniqueId())) return;
        try {
            FrameCloseEvent frameCloseEvent = new FrameCloseEvent((Player) entity, frames.get(entity.getUniqueId()));
            Bukkit.getPluginManager().callEvent(frameCloseEvent);
            if (frameCloseEvent.isCancelled()) return;
        }catch (Exception e){
            e.printStackTrace();
        }
        frames.remove(entity.getUniqueId());


    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {
            return;
        }

        PhoenixFrame frame = frames.get(entity.getUniqueId());
        if (frame == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() == null) {
            return;
        }

        PhoenixFrameComponent component = frame.getComponent(event.getSlot(), event.getClickedInventory().getType());
        if (component == null) {
            return;
        }

        ClickType click = event.getClick();
        Runnable listener = component.getListener(click, event.getClickedInventory().getType());
        if (listener == null) {
            return;
        }
        Player player = (Player) entity;
        String permission = component.getPermission(click, event.getClickedInventory().getType());
        if (permission != null) {
            if (!player.hasPermission(permission)) {
                if (guiDrawer == null) {
                    plugin.getLogger().warning(String.format("PhoenixGuiController failed to load " +
                            "WarningFrame for %s -> GuiDrawer is null!", player.getName()));
                    return;
                }
                guiDrawer.open(new WarningFrame(guiDrawer, frame, player, "lack of permission"));
                return;
            }
        }

        if (component.isConfirmationRequired(click, event.getClickedInventory().getType())) {
            if (guiDrawer == null) {
                plugin.getLogger().warning(String.format("PhoenixGuiController failed to load " +
                        "ConfirmationFrame for %s -> GuiDrawer is null!", player.getName()));
                return;
            }
            listener = () -> guiDrawer.open(new ConfirmationFrame(guiDrawer, frame, frame.getViewer(), component.getListener(click, event.getClickedInventory().getType())));
        }

        Runnable finalListener = listener;
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null) return;

                FrameComponentClickEvent frameComponentClickEvent = new FrameComponentClickEvent(player, frame, component);
                Bukkit.getPluginManager().callEvent(frameComponentClickEvent);
                if (frameComponentClickEvent.isCancelled()) {
                    return;
                }

                finalListener.run();
            } catch (Exception e) {
                e.printStackTrace();
                if (!(frame instanceof WarningFrame)) {
                    guiDrawer.open(new WarningFrame(guiDrawer, null, player,
                            "Unhandled error, contact with dev"));
                }else player.closeInventory();

            }
        });
    }

    public void register(@NotNull PhoenixFrame frame) {
        frames.put(frame.getViewer().getUniqueId(), frame);
    }

    public boolean isRegistered(@NotNull Player player) {
        return frames.containsKey(player.getUniqueId());
    }
}
