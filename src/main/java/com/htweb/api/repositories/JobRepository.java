package com.htweb.api.repositories;

import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Job;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface JobRepository extends BaseRepository<Job, Long> {
    PaginateResponse<Job> searchJobs(JobSearchRequest request, float[] queryVector);

    boolean isJobBelongToCompany(Long jobId, Long companyId);
    
    List<String> suggestKeywords(String query);
}
