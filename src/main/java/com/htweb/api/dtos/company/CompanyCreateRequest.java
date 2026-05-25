package com.htweb.api.dtos.company;

import com.htweb.api.dtos.locations.AddressCreateRequest;
import com.htweb.core.enums.CompanyType;
import com.htweb.core.enums.EmployeeSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreateRequest {
    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 255, message = "Tên công ty không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Loại hình công ty không được để trống")
    private CompanyType type;

    private EmployeeSize employeeSize;

    @NotBlank(message = "Mã số thuế không được để trống")
    private String taxCode;

    private String description;

    @NotNull(message = "Quốc gia không được để trống")
    private Long countryId;

    @NotNull(message = "Địa chỉ công ty không được để trống")
    private AddressCreateRequest address;
}