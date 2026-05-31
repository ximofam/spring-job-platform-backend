package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.job.JobCreateRequest;
import com.htweb.api.dtos.job.JobDetailResponse;
import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.api.dtos.job.JobSimpleResponse;
import com.htweb.api.services.JobService;
import com.htweb.core.helpers.paginates.PaginateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class ApiJobController {
    @Qualifier("apiJobService")
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<PaginateResponse<JobSimpleResponse>> searchJobs(
            @ModelAttribute @Valid JobSearchRequest request) {

        return ResponseEntity.ok(jobService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResponse> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('job:create')")
    public ResponseEntity<ApiResponse> createJob(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid JobCreateRequest request) {

        Long jobId = jobService.createJob(userId, request);
        ApiResponse res = new ApiResponse(
                "Đã tạo thành công job",
                Map.of("jobId", jobId)
        );
        
        return ResponseEntity.status(201).body(res);
    }


    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('job:create')")
    public ResponseEntity<Void> publishJob(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {

        jobService.publishJob(userId, id);

        return ResponseEntity.ok().build();
    }
}
