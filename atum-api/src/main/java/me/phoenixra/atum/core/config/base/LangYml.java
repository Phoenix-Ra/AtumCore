package me.phoenixra.atum.core.config.base;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.config.BaseConfig;
import me.phoenixra.atum.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

public class LangYml extends BaseConfig {
    public LangYml(@NotNull AtumPlugin plugin, @NotNull String configName, @NotNull ConfigType configType) {
        super(plugin, configName, configType);
    }

    public String getPluginPrefix(){
        return getFormattedString("prefix");
    }
    public String getMessage(@NotNull String msgPath){
        return getFormattedString("messages."+msgPath);
    }
}
