package me.phoenixra.atum.core.gui.events;

import me.phoenixra.atum.core.gui.api.GuiFrame;
import me.phoenixra.atum.core.gui.api.GuiComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GuiComponentClickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final GuiFrame frame;
    private final GuiComponent component;
    private boolean cancelled;

    public GuiComponentClickEvent(@NotNull Player player, @NotNull GuiFrame frame, @NotNull GuiComponent component) {
        super(player);
        this.frame = frame;
        this.component = component;
    }

    public @NotNull GuiFrame getFrame() {
        return frame;
    }

    public @NotNull GuiComponent getComponent() {
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
