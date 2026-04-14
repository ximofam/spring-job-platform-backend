package com.htweb.api.exceptions.users;

import com.htweb.api.exceptions.http.BaseHttpException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseHttpException {
    private static final String CODE = "USER_NOT_FOUND";

    public UserNotFoundException(String username) {
        super(
                HttpStatus.NOT_FOUND,
                CODE,
                "Not found user with username: %s",
                username
        );
    }
}
