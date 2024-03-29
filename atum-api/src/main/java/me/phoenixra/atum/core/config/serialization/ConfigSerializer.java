package me.phoenixra.atum.core.config.serialization;

import me.phoenixra.atum.core.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Save objects to configs.
 *
 * @param <T> The type of object to save
 */
@FunctionalInterface
public interface ConfigSerializer<T> {

    /**
     * Save an object to a config.
     * <p>Use AtumAPI#getInstance#createConfig</p>
     *
     * @param obj The object.
     * @return The config.
     */
    @NotNull
    Config serializeToConfig(@NotNull T obj);
}
