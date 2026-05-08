package com.htweb.api.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegisterCandidateRequest {
    private String name;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private Long countryId;
}
