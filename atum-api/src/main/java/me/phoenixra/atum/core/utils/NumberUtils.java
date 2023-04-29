package me.phoenixra.atum.core.utils;

import me.phoenixra.atum.core.exceptions.AtumException;
import org.jetbrains.annotations.NotNull;

public class NumberUtils {
    private NumberUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
    /**
     * Get if the string can be parsed to a number
     *
     * @param value the string
     * @param allowPoint allow number with point
     * @return if string is a number
     */
    public static boolean isNumber(@NotNull String value, boolean allowPoint){
        if(allowPoint) return value.matches("[0-9]+\\.[0-9]+|-[0-9]+\\.[0-9]+|[0-9]+|-[0-9]+");
        else return value.matches("[0-9]+|-[0-9]+");
    }
}
