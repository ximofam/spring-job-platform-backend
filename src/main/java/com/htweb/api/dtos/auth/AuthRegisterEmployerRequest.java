package com.htweb.api.dtos.auth;

import com.htweb.core.enums.CompanyType;
import com.htweb.core.enums.EmployeeSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AuthRegisterEmployerRequest extends AuthRegisterRequest {
    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 255, message = "Tên công ty quá dài")
    private String companyName;

    @NotNull(message = "Vui lòng tải lên logo công ty")
    private MultipartFile companyLogo;

    @NotNull(message = "Vui lòng chọn loại hình công ty")
    private CompanyType companyType;

    @NotNull(message = "Vui lòng chọn quy mô nhân sự")
    private EmployeeSize companyEmployeeSize;

    private String companyDescription;

    @NotBlank(message = "Mã số thuế không được để trống")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "Mã số thuế phải từ 10-13 chữ số")
    private String companyTaxCode;

    @NotNull(message = "Vui lòng chọn quốc gia")
    private Long companyCountryId;
}
