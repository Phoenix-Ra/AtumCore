package me.phoenixra.atum.core.events;

import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public interface EventManager {

    void registerListener(@NotNull Listener listener);


    void unregisterListener(@NotNull Listener listener);

    void unregisterAllListeners();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
