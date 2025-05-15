package com.vulkantechnologies.menu.exception;

public class MenuConfigurationLoadException extends RuntimeException {

    public MenuConfigurationLoadException(String message) {
        super(message);
    }

    public MenuConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuConfigurationLoadException(Throwable cause) {
        super(cause);
    }

    public MenuConfigurationLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
