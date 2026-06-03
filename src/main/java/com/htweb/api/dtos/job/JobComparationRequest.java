package com.htweb.api.dtos.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class JobComparationRequest {
    @NotBlank(message = "Vui lòng nhập mô tả bản thân")
    private String description;

    @NotEmpty(message = "Vui lòng chọn ít nhất 1 job")
    private List<Long> jobIds;

}
