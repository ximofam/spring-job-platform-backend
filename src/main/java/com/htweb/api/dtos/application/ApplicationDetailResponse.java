package com.htweb.api.dtos.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.htweb.api.dtos.user.UserDetailResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDetailResponse {
    private Long id;
    private ApplicationJobResponse job;
    private CandidateCvResponse cvFile;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant appliedAt;
    private String note = "";
    private List<ReviewResponse> reviews;
    private UserDetailResponse candidate;
}
