package com.htweb.exceptions.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final HttpStatus status;

    protected BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    protected BaseException(HttpStatus status, String format, Object... args) {
        super(String.format(format, args));
        this.status = status;
    }
}
