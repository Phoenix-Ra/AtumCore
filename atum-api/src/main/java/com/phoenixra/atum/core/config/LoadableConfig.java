package com.phoenixra.atum.core.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public interface LoadableConfig extends Config {
    /**
     * Create the file.
     */
    void createFile();


    /**
     * Save the config.
     *
     * @throws IOException If error has occurred while saving
     */
    void save() throws IOException;

    /**
     * Get the config file.
     *
     * @return The file.
     */
    File getFile();

    /**
     * Get the full file name
     *
     * @return The file name.
     */
    default String getFileName(){
        return getFile().getName();
    }
    /**
     * Get the name of a config
     *
     * @return The name.
     */
    default String getName(){
        return getFileName().replace("."+getType().getFileExtension(),"");
    }

    /**
     * Convert the config to a  {@link YamlConfiguration}.
     */
    @NotNull
    YamlConfiguration toBukkit();
}
