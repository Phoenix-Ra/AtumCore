package me.phoenixra.atum.core.gui;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import org.jetbrains.annotations.NotNull;

public interface GuiDrawer {


    /**
     * Open the frame
     * @param frame the frame
     */
    void open(@NotNull GuiFrame frame);

    /**
     * Update the frame
     * <p></p>
     * Use this, rather than {@link GuiDrawer#open(GuiFrame)}
     * in order to update the frame much faster
     *
     * @param frame the frame
     */
    void update(@NotNull GuiFrame frame);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
