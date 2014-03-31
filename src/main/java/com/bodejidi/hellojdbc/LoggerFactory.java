package com.bodejidi.hellojdbc;

public class LoggerFactory {
    static Logger getLogger(Class clazz) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
