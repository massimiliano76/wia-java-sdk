package io.wia.exception;

public class APIConnectionException extends WiaException {

    private static final long serialVersionUID = 1L;

    public APIConnectionException(String message) {
        super(message, null, 0);
    }

    public APIConnectionException(String message, Throwable e) {
        super(message, null, 0, e);
    }

}
