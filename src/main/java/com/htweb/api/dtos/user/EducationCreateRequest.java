package com.htweb.api.dtos.user;

import com.htweb.core.enums.Degree;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EducationCreateRequest {
    @NotBlank(message = "School is required")
    @Size(max = 200, message = "School must not exceed 200 characters")
    private String school;

    @Size(max = 150, message = "Major must not exceed 150 characters")
    @NotBlank(message = "Major is required")
    private String major;

    @NotNull(message = "Degree is required")
    private Degree degree;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String description;
}
