package me.phoenixra.atum.core.placeholders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AtumPlaceholder {
    String replace(@NotNull Player player,@Nullable String arg);
    String getPlaceholder();
}
