package me.phoenixra.atum.core.placeholders.types;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.placeholders.RegistrablePlaceholder;
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
public class PlayerlessPlaceholder implements RegistrablePlaceholder {
    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final Supplier<@Nullable String> function;

    /**
     * The plugin for the arguments.
     */
    private final AtumPlugin plugin;

    /**
     * Create a new player arguments.
     *
     * @param plugin     The plugin.
     * @param identifier The identifier.
     * @param function   The function to retrieve the value.
     */
    public PlayerlessPlaceholder(@NotNull final AtumPlugin plugin,
                                 @NotNull final String identifier,
                                 @NotNull final Supplier<@Nullable String> function) {
        this.plugin = plugin;
        this.pattern = Pattern.compile("%"+plugin.getName()+"_"+identifier+"%");
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        return function.get();
    }

    @Override
    public @NotNull AtumPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public @NotNull PlayerlessPlaceholder register() {
        return (PlayerlessPlaceholder) RegistrablePlaceholder.super.register();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerlessPlaceholder that)) {
            return false;
        }
        return Objects.equals(this.getPattern(), that.getPattern())
                && Objects.equals(this.getPlugin(), that.getPlugin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern(), this.getPlugin());
    }
}
