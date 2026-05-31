package com.htweb.api.services;

import com.htweb.api.dtos.job.JobCreateRequest;
import com.htweb.api.dtos.job.JobDetailResponse;
import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.api.dtos.job.JobSimpleResponse;
import com.htweb.core.helpers.paginates.PaginateResponse;

public interface JobService {
    PaginateResponse<JobSimpleResponse> search(JobSearchRequest request);

    JobDetailResponse getJob(Long id);

    Long createJob(Long userId, JobCreateRequest request);

    void publishJob(Long userId, Long jobId);
}
