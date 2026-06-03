package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.job.*;
import com.htweb.api.services.JobService;
import com.htweb.core.helpers.paginates.PaginateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class ApiJobController {
    @Qualifier("apiJobService")
    private final JobService jobService;

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggestKeywords(@RequestParam("q") String q) {
        List<String> res = jobService.suggestKeywords(q);
        return ResponseEntity.ok(res);
    }

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

    @PostMapping("/compare")
    public ResponseEntity<List<JobComparationResponse>> compareJobs(
            @RequestBody @Valid JobComparationRequest request) {
        return ResponseEntity.ok(jobService.compareJobs(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('job:update')")
    public ResponseEntity<Void> updateJob(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody @Valid JobUpdateRequest request) {

        jobService.updateJob(userId, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('job:delete')")
    public ResponseEntity<Void> deleteJob(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {

        jobService.deleteJob(userId, id);

        return ResponseEntity.ok().build();
    }
}
