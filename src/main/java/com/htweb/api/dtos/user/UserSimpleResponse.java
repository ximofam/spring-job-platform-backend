package com.htweb.api.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSimpleResponse {
    private Long id;
    private String username;
    private String name;
    private String avatarUrl;
}
