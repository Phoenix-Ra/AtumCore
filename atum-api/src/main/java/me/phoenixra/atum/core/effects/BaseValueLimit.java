package me.phoenixra.atum.core.effects;

import lombok.Getter;
import me.phoenixra.atum.core.effects.interfaces.ValueLimit;
import org.jetbrains.annotations.NotNull;

public class BaseValueLimit<T extends Number> implements ValueLimit<T> {
    @Getter private final T min;
    @Getter private final T max;
    public BaseValueLimit(T min, T max){
        this.min = min;
        this.max = max;
    }


    @NotNull
    @Override
    public T limit(@NotNull T value) {
        //@TODO
        return value;
    }

    @Override
    public ValueLimit<T> clone() throws CloneNotSupportedException {
        return null;
    }
}
