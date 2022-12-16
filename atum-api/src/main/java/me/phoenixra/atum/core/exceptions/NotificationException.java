package me.phoenixra.atum.core.exceptions;

import lombok.Getter;

public class NotificationException extends Exception{
    @Getter private final String message;
    @Getter private final boolean langKey;

    public NotificationException(String msg, boolean langKey) {
        super(msg);
        this.message = msg;
        this.langKey=langKey;
    }
}
