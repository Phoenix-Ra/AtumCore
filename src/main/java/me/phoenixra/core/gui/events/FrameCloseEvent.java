package me.phoenixra.core.gui.events;

import me.phoenixra.core.gui.api.PhoenixFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FrameCloseEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final @NotNull PhoenixFrame frame;

    public FrameCloseEvent(@NotNull Player viewer, @NotNull PhoenixFrame frame) {
        super(viewer);
        this.frame = frame;
    }

    public @NotNull PhoenixFrame getFrame() {
        return frame;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}