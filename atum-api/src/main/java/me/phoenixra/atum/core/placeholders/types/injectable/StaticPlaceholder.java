package me.phoenixra.atum.core.placeholders.types.injectable;


import me.phoenixra.atum.core.placeholders.InjectablePlaceholder;
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext;
import me.phoenixra.atum.core.utils.ObjectUtils;
import me.phoenixra.atum.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Placeholder which cannot be registered and exists only in injection
 */
public final class StaticPlaceholder implements InjectablePlaceholder {

    private final String identifier;


    private final Pattern pattern;


    private final Supplier<@Nullable String> function;



    public StaticPlaceholder(@NotNull final String identifier,
                             @NotNull final Supplier<@Nullable String> function) {
        this.identifier = "%" + identifier + "%";
        this.pattern = Pattern.compile(this.identifier, Pattern.LITERAL);
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        return function.get();
    }

    @Override
    public String tryTranslateQuickly(@NotNull final String text,
                                      @NotNull final PlaceholderContext context) {
        return StringUtils.replaceFast(
                text,
                this.identifier,
                ObjectUtils.requireNonNullElse(this.getValue(this.identifier, context), "")
        );
    }

    @Override
    public String toString() {
        return "StaticPlaceholder[identifier=" + this.identifier + "]";
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaticPlaceholder)) {
            return false;
        }
        final StaticPlaceholder that = (StaticPlaceholder) o;
        return Objects.equals(this.getPattern(), that.getPattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern());
    }
}
