package me.phoenixra.atum.core.config;

import me.phoenixra.atum.core.AtumAPI;
import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.config.wrapper.LoadableConfigWrapper;
import org.jetbrains.annotations.NotNull;

public class BaseConfig extends LoadableConfigWrapper {
    protected BaseConfig(@NotNull AtumPlugin plugin,
                         @NotNull String configName,
                         @NotNull ConfigType configType) {
        super(AtumAPI.getInstance().createLoadableConfig(
                plugin,
                configName,
                "",
                configType,
                false)
        );
    }
}
