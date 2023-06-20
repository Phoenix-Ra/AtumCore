package me.phoenixra.atum.core.events;

import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface EventManager {

    void callEvent(@NotNull Event event);

    /**
     * Register event listener
     *
     * @param listener The listener
     */
    void registerListener(@NotNull Listener listener);

    /**
     * Unregister event listener
     *
     * @param listener The listener
     */
    void unregisterListener(@NotNull Listener listener);

    /**
     * Unregister all event listeners
     *
     */
    void unregisterAllListeners();

    /**
     * Get the set of registered event listeners
     *
     * @return the listeners set
     */
    @NotNull Set<Listener> getRegisteredListeners();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
