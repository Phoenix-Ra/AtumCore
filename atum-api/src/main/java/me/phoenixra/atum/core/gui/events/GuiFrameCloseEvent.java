package me.phoenixra.atum.core.gui.events;

import me.phoenixra.atum.core.gui.api.GuiFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GuiFrameCloseEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final @NotNull GuiFrame frame;

    public GuiFrameCloseEvent(@NotNull Player viewer, @NotNull GuiFrame frame) {
        super(viewer);
        this.frame = frame;
    }

    public @NotNull GuiFrame getFrame() {
        return frame;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
