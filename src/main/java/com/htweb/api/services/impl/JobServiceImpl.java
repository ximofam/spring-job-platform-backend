package com.htweb.api.services.impl;

import com.htweb.api.dtos.job.JobCreateRequest;
import com.htweb.api.dtos.job.JobDetailResponse;
import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.api.dtos.job.JobSimpleResponse;
import com.htweb.api.exceptions.http.ForbiddenException;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.mappers.JobMapper;
import com.htweb.api.repositories.CompanyRepository;
import com.htweb.api.repositories.JobRepository;
import com.htweb.api.services.JobService;
import com.htweb.core.enums.JobStatus;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service("apiJobService")
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
public class JobServiceImpl implements JobService {
    @Qualifier("apiJobRepository")
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    @Qualifier("apiCompanyRepository")
    private final CompanyRepository companyRepository;
    @Value("${app.job.expiration-minutes:1440}")
    private long expirationMinutes;

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse<JobSimpleResponse> search(JobSearchRequest request) {
        PaginateResponse<Job> paginate = jobRepository.searchJobs(request);

        return paginate.map(jobMapper::toJobSimpleResponseList);
    }

    @Override
    @Transactional(readOnly = true)
    public JobDetailResponse getJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy công việc này"));

        return jobMapper.toJobDetailResponse(job);
    }

    @Override
    @Transactional
    public Long createJob(Long userId, JobCreateRequest request) {
        Job job = jobMapper.toJob(request);
        Company company = companyRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy công ty của bạn"));

        job.setCompany(company);

        jobRepository.save(job);

        return job.getId();
    }

    @Override
    @Transactional
    public void publishJob(Long userId, Long jobId) {
        Company company = companyRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy công ty của bạn"));

        if (!jobRepository.isJobBelongToCompany(jobId, company.getId())) {
            throw new ForbiddenException("Bạn không được phép");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Không tìm thầy công việc này"));
        Instant now = Instant.now();
        job.setStatus(JobStatus.PUBLISHED);
        job.setPublishedAt(now);
        job.setExpiredAt(now.plus(expirationMinutes, ChronoUnit.MINUTES));
        jobRepository.update(job);
    }

}
