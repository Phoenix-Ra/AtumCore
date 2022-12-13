package com.phoenixra.atum.core;

import com.phoenixra.atum.core.config.ConfigManager;
import com.phoenixra.atum.core.config.ConfigType;
import com.phoenixra.atum.core.config.LoadableConfig;
import com.phoenixra.atum.core.events.EventManager;
import com.phoenixra.atum.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Create a logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull AtumPlugin plugin);

    /**
     * create new config
     *
     * @param plugin The plugin.
     * @param name name of a config (without extension)
     * @param directory Optional. The directory of a config.
     * @param type The type of a config
     */
    LoadableConfig createLoadableConfig(@NotNull AtumPlugin plugin,
                                        @NotNull String name,
                                        @Nullable String directory,
                                        @NotNull ConfigType type);
    /**
     * load the config from plugin resource
     * <p></p>
     * <p></p>
     * The method tries to find the specified config
     * inside the plugin resource
     * <p></p>
     * If found -> creates new config file inside
     * the plugin folder with copied data from the resource
     * <p></p>
     * If config already exists, the method returns it
     *
     * @param plugin The plugin.
     * @param name name of a config (without extension)
     * @param directory Optional. The directory of a config.
     * @param type The type of a config
     * @return loaded config
     */
    @Nullable
    LoadableConfig createConfigFromResource(@NotNull AtumPlugin plugin,
                              @NotNull String name,
                              @Nullable String directory,
                              @NotNull ConfigType type);
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
