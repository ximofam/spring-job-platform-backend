package com.htweb.api.dtos.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.htweb.api.dtos.user.UserSimpleResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationSimpleResponse {
    private Long id;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant appliedAt;
    private ApplicationJobResponse job;
    private UserSimpleResponse candidate;
}
