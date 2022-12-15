package me.phoenixra.atum.core.gui;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GuiController extends Listener {

    /**
     * Register the frame
     *
     * @param frame the frame
     */
    void registerFrame(@NotNull GuiFrame frame);


    /**
     * Unregister all frames
     *
     */
    void unregisterAllFrames();

    /**
     * Unregister the frame
     *
     * @param viewer the frame viewer
     */
    void unregisterFrame(@NotNull Player viewer);


    /**
     * Get registered frames
     *
     * @return registered frames
     */
    @NotNull
    List<GuiFrame> getRegisteredFrames();

    /**
     * Returns frame opened by the player
     *
     * @param viewer the frame viewer
     * @return the frame
     */
    @Nullable
    GuiFrame getPlayerOpenedFrame(@NotNull Player viewer);

    /**
     * Get GuiDrawer
     *
     * @return The GuiDrawer
     */
    @NotNull
    GuiDrawer getGuiDrawer();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
