package com.bodejidi.hellojdbc;

public class ValidateException extends ApplicationRuntimeException {
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
