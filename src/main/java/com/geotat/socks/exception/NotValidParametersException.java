package com.geotat.socks.exception;

import lombok.Getter;

@Getter
public class NotValidParametersException extends RuntimeException {
    private final String message;

    public NotValidParametersException(String message) {
        this.message = message;
    }
}
