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
     * @param async whether to update the frame asynchronously
     */
    void update(@NotNull GuiFrame frame, boolean async);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
