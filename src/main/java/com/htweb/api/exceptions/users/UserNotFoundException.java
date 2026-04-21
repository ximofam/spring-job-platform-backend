package com.htweb.api.exceptions.users;

import com.htweb.api.exceptions.http.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final String CODE = "USER_NOT_FOUND";

    public UserNotFoundException() {
        super(CODE, "This user not found");
    }
}
