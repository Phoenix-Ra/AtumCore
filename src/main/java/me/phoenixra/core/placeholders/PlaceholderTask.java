package me.phoenixra.core.placeholders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlaceholderTask {
    String getReplacement(@NotNull Player player,@Nullable String arg);
    String getPlaceholder();
}
