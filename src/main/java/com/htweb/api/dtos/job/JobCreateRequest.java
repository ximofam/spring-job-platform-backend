package com.htweb.api.dtos.job;

import com.htweb.api.dtos.locations.AddressCreateRequest;
import com.htweb.core.enums.EmploymentType;
import com.htweb.core.enums.ExperienceLevel;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobCreateRequest {
    @NotBlank(message = "Tiêu đề công việc không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    @NotNull(message = "Địa chỉ làm việc không được để trống")
    private AddressCreateRequest address;

    @NotNull(message = "Loại hình làm việc không được để trống")
    private EmploymentType employmentType;

    @NotNull(message = "Cấp bậc kinh nghiệm không được để trống")
    private ExperienceLevel experienceLevel;

    @NotBlank(message = "Mô tả công việc không được để trống")
    private String description;

    @NotBlank(message = "Yêu cầu công việc không được để trống")
    private String requirements;

    @NotBlank(message = "Quyền lợi không được để trống")
    private String benefit;

    @PositiveOrZero(message = "Mức lương tối thiểu phải lớn hơn hoặc bằng 0")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Mức lương tối đa phải lớn hơn hoặc bằng 0")
    private BigDecimal salaryMax;

    @NotBlank(message = "Đơn vị tiền tệ không được để trống")
    private String salaryCurrency = "VND";

    @AssertTrue(message = "Lương tối đa phải lớn hơn hoặc bằng lương tối thiểu")
    private boolean isSalaryValid() {
        if (salaryMin == null || salaryMax == null) {
            return true;
        }
        return salaryMax.compareTo(salaryMin) >= 0;
    }
}
