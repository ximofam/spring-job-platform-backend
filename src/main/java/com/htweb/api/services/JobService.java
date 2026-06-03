package com.htweb.api.services;

import com.htweb.api.dtos.job.*;
import com.htweb.core.helpers.paginates.PaginateResponse;

import java.util.List;

public interface JobService {
    PaginateResponse<JobSimpleResponse> search(JobSearchRequest request);

    JobDetailResponse getJob(Long id);

    Long createJob(Long userId, JobCreateRequest request);

    void publishJob(Long userId, Long jobId);

    List<String> suggestKeywords(String query);

    List<JobComparationResponse> compareJobs(JobComparationRequest request);

    List<MyJobResponse> getMyJobs(Long userId);

    MyJobDetailResponse getMyJobById(Long userId, Long jobId);

    void updateJob(Long userId, Long jobId, JobUpdateRequest request);

    void deleteJob(Long userId, Long jobId);
}
