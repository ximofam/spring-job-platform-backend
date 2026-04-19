package com.htweb.api.exceptions.users;

import com.htweb.api.exceptions.http.UnauthorizedException;

public class IncorrectUsernameOrPasswordException extends UnauthorizedException {

    public IncorrectUsernameOrPasswordException() {
        super("Incorrect username or password");
    }
}
