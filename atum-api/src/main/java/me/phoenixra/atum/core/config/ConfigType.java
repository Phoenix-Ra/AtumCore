package me.phoenixra.atum.core.config;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum ConfigType {
    /**
     * .json file
     */
    JSON("json"),

    /**
     * .yml file
     */
    YAML("yml"),

    /**
     * .toml file
     */
    TOML("toml");


    @Getter
    private final String fileExtension;

    ConfigType(@NotNull final String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
