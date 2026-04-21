package com.htweb.api.exceptions.tokens;

import com.htweb.api.exceptions.http.UnauthorizedException;

public class TokenInvalidException extends UnauthorizedException {
    public TokenInvalidException() {
        super("Invalid token");
    }

    public TokenInvalidException(String format, Object... args) {
        super(format, args);
    }

    public TokenInvalidException(String msg) {
        super(msg);
    }
}
