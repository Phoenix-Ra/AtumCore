package me.phoenixra.atum.core.utils;

public class ObjectUtils {

    //require non null else
    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        return obj == null ? defaultObj : obj;
    }
    //non null checker
    public static <T> boolean nonNull(T obj) {
        return obj != null;
    }
}
