package com.bodejidi.hellojdbc;

public class DataAccessException extends ApplicationRuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
