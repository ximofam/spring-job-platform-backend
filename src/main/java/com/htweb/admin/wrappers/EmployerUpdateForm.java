package com.htweb.admin.wrappers;

import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EmployerUpdateForm {
    private User user;
    private EmployerProfile profile;
    private Company company;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Set<Role> roles = new HashSet<>();
}
