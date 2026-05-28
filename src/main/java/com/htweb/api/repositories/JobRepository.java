package com.htweb.api.repositories;

import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Job;
import com.htweb.core.repositories.BaseRepository;

public interface JobRepository extends BaseRepository<Job, Long> {
    PaginateResponse<Job> searchJobs(JobSearchRequest request);

    boolean isJobBelongToCompany(Long jobId, Long companyId);
}
