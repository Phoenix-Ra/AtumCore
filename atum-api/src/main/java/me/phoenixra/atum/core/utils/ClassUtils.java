package me.phoenixra.atum.core.utils;

import org.jetbrains.annotations.NotNull;

public class ClassUtils {

    /**
     * Get if class exists
     *
     * @param classPach the class path
     * @return if string is a number
     */
    public static boolean exists(@NotNull final String classPach) {
        try {
            Class.forName(classPach);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private ClassUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
