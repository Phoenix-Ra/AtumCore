package me.phoenixra.atum.core.config;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.config.category.ConfigCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigManager {

    /**
     * load all configs from disk
     */
    void reloadAllConfigs();

    /**
     * load the specified config from disk
     *
     * @param name the name of a config
     */
    void reloadConfig(@NotNull String name);

    /**
     * Save all configs.
     */
    void saveAllConfigs();

    /**
     * Save the specified config
     *
     * @param name the name of a config
     */
    void saveConfig(@NotNull String name);

    /**
     * get config added to the manager
     * or null if not found
     * @param name The name of a config
     * @return this
     */
    @Nullable
    LoadableConfig getConfig(@NotNull String name);
    /**
     * Add new config to the handler
     *
     * @param config The loadable config
     * @return this
     */
    ConfigManager addConfig(@NotNull LoadableConfig config);

    /**
     * reload all config categories
     */
    void reloadAllConfigCategories();

    /**
     * reload the config category
     *
     * @param id the id
     */
    void reloadConfigCategory(@NotNull String id);

    /**
     * get config category added to the manager
     * or null if not found
     * @param id The id
     * @return The config category
     */
    @Nullable
    ConfigCategory getConfigCategory(@NotNull String id);

    /**
     * Add new config category
     *
     * @param configCategory The config category
     */
    void addConfigCategory(@NotNull ConfigCategory configCategory);

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
