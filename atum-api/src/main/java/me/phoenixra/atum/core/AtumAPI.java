package me.phoenixra.atum.core;

import me.phoenixra.atum.core.config.ConfigManager;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.LoadableConfig;
import me.phoenixra.atum.core.events.EventManager;
import me.phoenixra.atum.core.gui.GuiController;
import me.phoenixra.atum.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

public interface AtumAPI {
    /**
     * Create a scheduler.
     *
     * @param plugin The plugin.
     * @return The scheduler.
     */
    @NotNull
    Scheduler createScheduler(@NotNull AtumPlugin plugin);

    /**
     * Create an event manager.
     *
     * @param plugin The plugin.
     * @return The event manager.
     */
    @NotNull
    EventManager createEventManager(@NotNull AtumPlugin plugin);
    /**
     * Create a config handler.
     *
     * @param plugin The plugin.
     * @return The handler.
     */
    @NotNull
    ConfigManager createConfigManager(@NotNull AtumPlugin plugin);

    /**
     * Create GuiController
     *
     * @param plugin The plugin.
     * @return The controller
     */
    @NotNull
    GuiController createGuiController(@NotNull AtumPlugin plugin);

    /**
     * Create a logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull AtumPlugin plugin);

    /**
     * loads an existing config from plugin folder
     * <p></p>
     * if specified config doesn't exists
     * creates a new config with content
     * from the plugin resources
     * <p></p>
     *
     * @param plugin The plugin.
     * @param name name of a config (without extension)
     * @param directory The directory of a config. Use empty String if root folder
     * @param type The type of a config
     * @param forceLoadResource if true -> throws NullPointerException
     *                       when file not found inside the resources folder,
     *                          otherwise creates an empty file
     * @return loaded config
     */
    @NotNull
    LoadableConfig createLoadableConfig(@NotNull AtumPlugin plugin,
                              @NotNull String name,
                              @NotNull String directory,
                              @NotNull ConfigType type,
                              boolean forceLoadResource);
    /**
     * Get plugin by name
     *
     * @param name The name.
     * @return the plugin.
     */
    @Nullable
    AtumPlugin getPluginByName(@NotNull String name);

    /**
     * Get loaded plugins
     *
     * @param name The name.
     * @return the plugin.
     */
    @NotNull
    List<AtumPlugin> getLoadedPlugins(@NotNull String name);
    /**
     * Add new plugin.
     *
     * @param plugin The plugin.
     */
    void addPlugin(@NotNull AtumPlugin plugin);

    /**
     * remove the plugin.
     *
     * @param plugin The plugin.
     */
    void removePlugin(@NotNull AtumPlugin plugin);
}
