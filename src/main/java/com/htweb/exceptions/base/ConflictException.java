package com.htweb.exceptions.base;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public ConflictException(String format, Object... args) {
        super(HttpStatus.CONFLICT, format, args);
    }
}