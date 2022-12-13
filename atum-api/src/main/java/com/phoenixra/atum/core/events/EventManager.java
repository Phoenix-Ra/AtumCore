package com.phoenixra.atum.core.events;

import com.phoenixra.atum.core.AtumPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public interface EventManager {

    void registerListener(@NotNull Listener listener);


    void unregisterListener(@NotNull Listener listener);

    void unregisterAllListeners();

    AtumPlugin getPlugin();
}
