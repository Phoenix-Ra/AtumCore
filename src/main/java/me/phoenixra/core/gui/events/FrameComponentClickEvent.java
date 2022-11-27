package me.phoenixra.core.gui.events;

import me.phoenixra.core.gui.api.PhoenixFrame;
import me.phoenixra.core.gui.api.PhoenixFrameComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FrameComponentClickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final PhoenixFrame frame;
    private final PhoenixFrameComponent component;
    private boolean cancelled;

    public FrameComponentClickEvent(@NotNull Player player, @NotNull PhoenixFrame frame, @NotNull PhoenixFrameComponent component) {
        super(player);
        this.frame = frame;
        this.component = component;
    }

    public @NotNull PhoenixFrame getFrame() {
        return frame;
    }

    public @NotNull PhoenixFrameComponent getComponent() {
        return component;
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
}
