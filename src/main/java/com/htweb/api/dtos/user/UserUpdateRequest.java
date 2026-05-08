package com.htweb.api.dtos.user;

import com.htweb.core.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private String phone;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Long countryId;
    private String address;
    private MultipartFile avatar;
}
