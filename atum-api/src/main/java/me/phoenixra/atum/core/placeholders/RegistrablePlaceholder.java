package me.phoenixra.atum.core.placeholders;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

public interface RegistrablePlaceholder extends Placeholder{

    /**
     * Register the arguments.
     *
     * @return The arguments.
     */
    @NotNull
    default RegistrablePlaceholder register() {
        return this;
    }

    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @NotNull
    @Override
    AtumPlugin getPlugin();
}
