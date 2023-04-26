package me.phoenixra.atum.core.utils;

import org.jetbrains.annotations.NotNull;

public class ClassUtils {

    public static boolean exists(@NotNull final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private ClassUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
