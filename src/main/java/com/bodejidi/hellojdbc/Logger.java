package com.bodejidi.hellojdbc;

public class Logger {

    private org.slf4j.Logger logger;

    public Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void trace(String message) {
        this.logger.trace(message);
    }

    public void trace(String message, Throwable e) {
        this.logger.trace(message, e);
    }

    public void debug(String message) {
        this.logger.debug(message);
    }

    public void debug(String message, Throwable e) {
        this.logger.debug(message, e);
    }

    public void info(String message) {
        this.logger.info(message);
    }

    public void info(String message, Throwable e) {
        this.logger.info(message, e);
    }

    public void warn(String message) {
        this.logger.warn(message);
    }

    public void warn(String message, Throwable e) {
        this.logger.warn(message, e);
    }

    public void error(String message) {
        this.logger.error(message);
    }

    public void error(String message, Throwable e) {
        this.logger.error(message, e);
    }
}
