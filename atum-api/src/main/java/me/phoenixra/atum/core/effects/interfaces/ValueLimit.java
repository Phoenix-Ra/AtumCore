package me.phoenixra.atum.core.effects.interfaces;

import org.jetbrains.annotations.NotNull;

public interface ValueLimit<T extends Number> extends Cloneable{

    /**
     * get the max value
     *
     * @return The value
     */
    @NotNull
    T getMax();

    /**
     * get the min value
     *
     * @return The value
     */
    @NotNull
    T getMin();

    /**
     * limit the given value
     *
     * @param value to limit
     * @return The result
     */
    @NotNull
    T limit(@NotNull T value);

    ValueLimit<T> clone() throws CloneNotSupportedException;
}
