package me.phoenixra.atum.core.effects;

import lombok.Getter;
import me.phoenixra.atum.core.effects.interfaces.EffectRunnable;
import me.phoenixra.atum.core.effects.interfaces.EffectVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class BaseEffectVariable<T> implements EffectVariable<T> {

    @Getter private T value;
    @Getter private EffectRunnable<T> taskPerIteration;
    private boolean forceCloneValue = false;

    public BaseEffectVariable(T value){
        this.value=value;
    }

    /**
     * Whether to force clone the value using java reflections.
     *
     * Use it with cautious. May produce lags
     *
     */
    public EffectVariable<T> setForceCloneValue(boolean value){
        forceCloneValue=value;
        return this;
    }
    @Override
    public @NotNull EffectVariable<T> setValue(@NotNull T value) {
        this.value=value;
        return this;
    }

    @Override
    public @NotNull EffectVariable<T> setTaskPerIteration(@NotNull EffectRunnable<T> task) {
        taskPerIteration=task;
        return this;
    }

    @Override
    public EffectVariable<T> clone() throws CloneNotSupportedException {
        EffectVariable<T> variable = (EffectVariable<T>) (super.clone());
        if(taskPerIteration!=null) {
            variable.setTaskPerIteration(taskPerIteration.clone());
        }
        if(forceCloneValue && value != null && value instanceof Cloneable){
            try {
                Method method =value.getClass().getMethod("clone");
                method.setAccessible(true);
                setValue((T)method.invoke(value));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return variable;

    }
}
