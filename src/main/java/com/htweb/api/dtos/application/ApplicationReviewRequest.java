package com.htweb.api.dtos.application;

import com.htweb.core.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationReviewRequest {
    @NotNull(message = "status không được để trông")
    private ApplicationStatus status;
    @NotBlank(message = "reason không được để trống")
    private String reason;
}
