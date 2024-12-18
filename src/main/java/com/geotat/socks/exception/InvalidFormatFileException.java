package com.geotat.socks.exception;

import lombok.Getter;

@Getter
public class InvalidFormatFileException extends RuntimeException {
    private final String message;

    public InvalidFormatFileException(String message) {
        this.message = message;
    }
}
