package com.htweb.api.exceptions.tokens;

import com.htweb.api.exceptions.http.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    public TokenExpiredException() {
        super("Token has been expired");
    }

    public TokenExpiredException(String format, Object... args) {
        super(format, args);
    }
}
