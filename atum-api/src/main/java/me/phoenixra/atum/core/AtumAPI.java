package me.phoenixra.atum.core;

import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigManager;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.LoadableConfig;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.events.EventManager;
import me.phoenixra.atum.core.gui.GuiController;
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext;
import me.phoenixra.atum.core.schedule.Scheduler;
import me.phoenixra.atum.core.scoreboard.Scoreboard;
import me.phoenixra.atum.core.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
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
     * Create a config manager
     *
     * @param plugin The plugin.
     * @return The manager
     */
    @NotNull
    ConfigManager createConfigManager(@NotNull AtumPlugin plugin);

    /**
     * Create a scoreboard manager
     *
     * @param plugin The plugin.
     * @return The manager
     */
    @NotNull
    ScoreboardManager createScoreboardManager(@NotNull AtumPlugin plugin);

    /**
     * Create GuiController
     *
     * @param plugin The plugin.
     * @return The controller
     */
    @NotNull
    GuiController createGuiController(@NotNull AtumPlugin plugin);

    /**
     * Create EffectsManager
     *
     * @param plugin The plugin.
     * @return The controller
     */
    @NotNull
    EffectsManager createEffectsManager(@NotNull AtumPlugin plugin);

    /**
     * Create a plugin logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull AtumPlugin plugin);

    /**
     * Create the scoreboard
     *
     * @param id the id of the scoreboard
     * @param displayName the scoreboard's displayName (each 2 ticks updates)
     * @param scores the scores
     * @return The scoreboard
     */
    @NotNull
    Scoreboard createScoreboard(@NotNull String id,
                                @NotNull List<String> displayName,
                                @NotNull List<String> scores);

    /**
     * Load configuration from file
     *
     * @param plugin The plugin.
     * @param file the file to load configuration from
     * @return loaded config
     */
    @NotNull
    LoadableConfig loadConfiguration(@NotNull AtumPlugin plugin,
                                     @NotNull File file);

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
     * @param directory The directory of a config. Use empty if root directory
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
     * Create config.
     *
     * @param values The values.
     * @param type   The config type.
     * @return The config
     */
    @NotNull
    Config createConfig(@Nullable Map<String, Object> values,
                        @NotNull ConfigType type);

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

    /**
     * Evaluate an expression.
     *
     * @param expression The expression.
     * @param context    The context.
     * @return The value of the expression, or zero if invalid.
     */
    double evaluate(@NotNull String expression,
                    @NotNull PlaceholderContext context);

    static AtumAPI getInstance() {
        return Instance.get();
    }

    final class Instance {
        private static AtumAPI api;
        private Instance() {
            throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
        }

        static void set(final AtumAPI api) {
            if(Instance.api != null) return;

            Instance.api = api;
        }


        static AtumAPI get() {
            return api;
        }


    }
}
