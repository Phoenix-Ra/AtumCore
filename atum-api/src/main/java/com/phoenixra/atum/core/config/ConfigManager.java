package com.phoenixra.atum.core.config;

import com.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

public interface ConfigManager {

    /**
     * load all configs from disk
     */
    void loadAllConfigs();

    /**
     * load the specified config from disk
     *
     * @param name the name of a config
     */
    void loadConfig(@NotNull String name);

    /**
     * reload all configs from disk
     */
    void reloadAllConfigs();

    /**
     * reload the specified config from disk
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
     * Add new config to the handler
     *
     * @param config The loadable config
     * @return this
     */
    ConfigManager addConfig(@NotNull LoadableConfig config);


    AtumPlugin getPlugin();
}
