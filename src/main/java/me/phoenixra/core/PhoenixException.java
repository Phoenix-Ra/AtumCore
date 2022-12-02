package me.phoenixra.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class PhoenixException extends Exception{
    private static final long serialVersionUID = 1L;
    private Exception cause;
    private String message;
    @Getter @Setter @Accessors(chain = true)
    private String messageToPlayer = "&8&oUnhandled error,\\n&8&o please contact with server administration";

    public PhoenixException(String msg) {
        this.init(this, msg);
    }

    public PhoenixException(Exception exception) {
        this.init(exception, exception.getMessage());
    }

    public PhoenixException(Exception exception, String msg) {
        this.init(exception, msg);
    }


    private void init(Exception exception, String msg) {
        this.cause = exception;
        this.message = msg;
    }

    @Override
    public Exception getCause() {
        return this.cause;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
