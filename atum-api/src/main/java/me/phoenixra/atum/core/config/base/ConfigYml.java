package me.phoenixra.atum.core.config.base;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.config.BaseConfig;
import me.phoenixra.atum.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

public class ConfigYml extends BaseConfig {

    public ConfigYml(@NotNull AtumPlugin plugin, @NotNull String configName, @NotNull ConfigType configType, boolean forceLoadResource) {
        super(plugin, configName, configType, forceLoadResource);
    }

    public boolean isDebugEnabled(){
        return getBool("debug");
    }
}