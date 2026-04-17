package com.htweb.api.dtos;

import java.util.List;

public class UserDto {
    private UserDto() {
    }

    public record DetailResponse(
            Long id,
            String username,
            List<String> roles
    ) {
    }
}
