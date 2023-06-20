package me.phoenixra.atum.core.placeholders.types.injectable;


import me.phoenixra.atum.core.placeholders.InjectablePlaceholder;
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public class PlayerStaticPlaceholder implements InjectablePlaceholder {
    /**
     * The identifier.
     */
    private final String identifier;

    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final Function<@NotNull Player, @Nullable String> function;

    /**
     * Create a new player arguments.
     *
     * @param identifier The identifier.
     * @param function   The function to retrieve the value.
     */
    public PlayerStaticPlaceholder(@NotNull final String identifier,
                                   @NotNull final Function<@NotNull Player, @Nullable String> function) {
        this.identifier = "%" + identifier + "%";
        this.pattern = Pattern.compile(this.identifier);
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        Player player = context.getPlayer();

        if (player == null) {
            return null;
        }

        return function.apply(player);
    }

    @Override
    public String tryTranslateQuickly(@NotNull final String text,
                                      @NotNull final PlaceholderContext context) {
        return StringUtils.replaceFast(
                text,
                this.identifier,
                Objects.requireNonNullElse(
                        this.getValue(identifier, context),
                        ""
                )
        );
    }
    @Override
    public String toString() {
        return "PlayerStaticPlaceholder[identifier=" + this.identifier + "]";
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
        if (!(o instanceof PlayerStaticPlaceholder that)) {
            return false;
        }
        return Objects.equals(this.getPattern(), that.getPattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern());
    }
}
