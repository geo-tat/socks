package com.geotat.socks.exception;

import lombok.Getter;

@Getter
public class SocksNotEnoughException extends RuntimeException {
    private final String message;

    public SocksNotEnoughException(String message) {
        this.message = message;
    }
}
