package me.phoenixra.atum.core.exceptions;

import lombok.Getter;
import me.phoenixra.atum.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * NotificationException
 * <p></p>
 * Useful exception which you can use to simplify
 * condition and null checks in
 * your commands or anywhere else.
 * @see NotificationException#notifyFalse(boolean, String)
 * @see NotificationException#notifyNull(Object, String)
 */
public class NotificationException extends Exception{
    private final String message;

    @Getter
    private final boolean langKey;

    public NotificationException(String msg, boolean langKey) {
        super(msg);
        this.message = msg;
        this.langKey = langKey;
    }
    public NotificationException(String msg) {
        this(msg, false);
    }

    /**
     * Get formatted Notification message
     *
     * @return the message
     */
    @Override
    public String getMessage() {
        return StringUtils.format(message);
    }

    /**
     * Basic notification method, that throws NotificationException
     * @param msg The msg to send
     * @param langKey if the msg is a lang key
     */
    public static void notify(@NotNull String msg, boolean langKey) throws NotificationException {

        throw new NotificationException(msg,langKey);
    }

    /**
     * Basic notification method, that throws NotificationException
     * @param msg The msg to send
     */
    public static void notify(@NotNull String msg) throws NotificationException {

        notify(msg,false);
    }

    /**
     * throws NotificationException when obj is null
     * @param obj an object to check
     * @param msg The msg to send
     * @param langKey if the msg is a lang key
     */
    public static @NotNull <T> T notifyNull(@Nullable T obj,
                                            @NotNull String msg,
                                            boolean langKey)
            throws NotificationException {

        if (obj==null) notify(msg,langKey);
        return obj;
    }

    /**
     * throws NotificationException when obj is null
     * @param obj an object to check
     * @param msg The msg to send
     */
    public static @NotNull <T> T notifyNull(@Nullable T obj,
                                            @NotNull String msg)
            throws NotificationException {
        return notifyNull(obj,msg,false);
    }

    /**
     * throws NotificationException when predicate#test(obj) returns false
     * @param obj an object to check
     * @param predicate the condition to test on the received object
     * @param msg The msg to send
     * @param langKey if the msg is a lang key
     */
    public static @NotNull <T> T notifyFalse(@NotNull T obj,
                                             @NotNull Predicate<T> predicate,
                                             @NotNull String msg,
                                             boolean langKey) throws NotificationException {
        notifyFalse(predicate.test(obj), msg,langKey);
        return obj;
    }

    /**
     * throws NotificationException when predicate#test(obj) returns false
     * @param obj an object to check
     * @param predicate the condition to test on the received object
     * @param msg The msg to send
     */
    public static @NotNull <T> T notifyFalse(@NotNull T obj,
                                             @NotNull Predicate<T> predicate,
                                             @NotNull String msg) throws NotificationException {
        return notifyFalse(obj,predicate,msg,false);
    }

    /**
     * throws NotificationException when condition is false
     * @param condition boolean value
     * @param msg The msg to send
     * @param langKey if the msg is a lang key
     */
    public static boolean notifyFalse(boolean condition,
                                      @NotNull String msg,
                                      boolean langKey) throws NotificationException {

        if (!condition) notify(msg,langKey);

        return true;
    }

    /**
     * throws NotificationException when condition is false
     * @param condition boolean value
     * @param msg The msg to send
     */
    public static boolean notifyFalse(boolean condition,
                                      @NotNull String msg) throws NotificationException {
        return notifyFalse(condition,msg,false);
    }

    /**
     * throws NotificationException when collection#size() is less than sizeRequired
     * @param collection the collection to check
     * @param sizeRequired the size required
     * @param msg The msg to send
     * @param langKey if the msg is a lang key
     */
    public static boolean notifyRequireSize(@NotNull Collection<?> collection,
                                            int sizeRequired,
                                            @NotNull String msg,
                                            boolean langKey) throws NotificationException {

        if (collection.size()<sizeRequired) notify(msg,langKey);

        return true;
    }

    /**
     * throws NotificationException when collection#size() is less than sizeRequired
     * @param collection the collection to check
     * @param sizeRequired the size required
     * @param msg The msg to send
     */
    public static boolean notifyRequireSize(@NotNull Collection<?> collection,
                                            int sizeRequired,
                                            @NotNull String msg) throws NotificationException {
        notifyRequireSize(collection,sizeRequired,msg,false);
        return true;
    }
}
