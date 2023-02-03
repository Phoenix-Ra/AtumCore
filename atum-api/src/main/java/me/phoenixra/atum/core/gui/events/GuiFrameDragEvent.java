package me.phoenixra.atum.core.gui.events;

import me.phoenixra.atum.core.gui.api.GuiFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GuiFrameDragEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final InventoryDragEvent parentEvent;
    private final GuiFrame frame;
    private boolean cancelled;

    public GuiFrameDragEvent(@NotNull Player player,
                              @NotNull GuiFrame frame,
                              @NotNull InventoryDragEvent event) {
        super(player);
        this.frame = frame;
        parentEvent = event;
    }
    public @NotNull InventoryDragEvent getParentEvent() {
        return parentEvent;
    }

    public @NotNull GuiFrame getFrame() {
        return frame;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
