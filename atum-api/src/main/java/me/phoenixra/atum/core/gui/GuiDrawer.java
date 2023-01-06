package me.phoenixra.atum.core.gui;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import org.jetbrains.annotations.NotNull;

public interface GuiDrawer {

    /**
     * Open the frame synchronously
     * @param frame the frame
     */
    default void open(@NotNull GuiFrame frame){
        open(frame,false);
    }

    /**
     * Open the frame
     * @param frame the frame
     * @param async whether to open the frame asynchronously
     */
    void open(@NotNull GuiFrame frame, boolean async);

    /**
     * Update the frame Synchronously
     * <p></p>
     * Use this, rather than {@link GuiDrawer#open(GuiFrame, boolean)}
     * in order to update the frame much faster
     *
     * @param frame the frame
     */
    default void update(@NotNull GuiFrame frame){
        update(frame, false);
    }
    /**
     * Update the frame
     * <p></p>
     * Use this, rather than {@link GuiDrawer#open(GuiFrame, boolean)}
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
