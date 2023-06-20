package me.phoenixra.atum.core.placeholders;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.Nullable;

public interface InjectablePlaceholder extends Placeholder{
    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @Nullable
    @Override
    default AtumPlugin getPlugin() {
        return null;
    }
}
