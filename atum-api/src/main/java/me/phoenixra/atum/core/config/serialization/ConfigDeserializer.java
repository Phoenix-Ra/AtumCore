package me.phoenixra.atum.core.config.serialization;

import me.phoenixra.atum.core.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Load objects from configs
 *
 * @param <T> The type of object to load
 */
@FunctionalInterface
public interface ConfigDeserializer<T> {

    /**
     * Load an object from config
     *
     * @param config The config.
     * @return The object.
     */
    @Nullable
    T deserializeFromConfig(@NotNull Config config);
}
