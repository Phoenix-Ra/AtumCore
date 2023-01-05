package me.phoenixra.atum.core.effects.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EffectVariable<T> extends Cloneable {
    /**
     * get the saved value
     *
     * @return The value
     */
    @NotNull
    T getValue();

    /**
     * set the value
     *
     * @return this
     */
    @NotNull
    EffectVariable<T> setValue(@NotNull T value);

    /**
     * get variable task
     *
     * @return the task
     */
    @Nullable
    EffectRunnable<T> getTaskPerIteration();

    /**
     * set variable task
     *
     * @return this
     */
    @NotNull
    EffectVariable<T> setTaskPerIteration(@NotNull EffectRunnable<T> task);

    /**
     * run the variable task
     *
     */
    default void runTask(@NotNull Effect effect){
        if(getTaskPerIteration()==null) return;
        setValue(getTaskPerIteration().run(effect,getValue()));
    }


    EffectVariable<T> clone() throws CloneNotSupportedException;
}
