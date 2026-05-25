package com.htweb.api.dtos.auth;

import com.htweb.api.dtos.company.CompanyCreateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegisterEmployerRequest extends AuthRegisterRequest {
    @NotNull(message = "Thông tin công ty không được để trống")
    private CompanyCreateRequest company;
}
