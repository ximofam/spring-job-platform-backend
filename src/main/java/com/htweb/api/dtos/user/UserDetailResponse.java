package com.htweb.api.dtos.user;

import java.util.List;

public record UserDetailResponse(
        Long id,
        String username,
        String email,
        String name,
        String avatarUrl,
        List<String> roles
) {
}
