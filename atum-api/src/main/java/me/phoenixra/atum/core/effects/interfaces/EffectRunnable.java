package me.phoenixra.atum.core.effects.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EffectRunnable<T> extends Cloneable{
    @NotNull
    T run(@NotNull Effect effect, @Nullable T var);

    EffectRunnable<T> clone() throws CloneNotSupportedException;
}
