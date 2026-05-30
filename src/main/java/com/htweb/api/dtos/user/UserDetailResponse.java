package com.htweb.api.dtos.user;

import com.htweb.api.dtos.country.CountryDetailResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailResponse {
    private String username;
    private String email;
    private String role;
    private String name;
    private String phone;
    private String avatarUrl;
    private String gender;
    private String address;
    private String dateOfBirth;
    private CountryDetailResponse country;
    private Object profile;
}
