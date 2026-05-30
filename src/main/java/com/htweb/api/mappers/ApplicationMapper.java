package com.htweb.api.mappers;

import com.htweb.api.dtos.application.*;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.repositories.CandidateCvRepository;
import com.htweb.api.repositories.JobRepository;
import com.htweb.core.pojo.Application;
import com.htweb.core.pojo.CandidateCv;
import com.htweb.core.pojo.Job;
import com.htweb.core.pojo.ReviewApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CandidateProfileMapper.class})
public abstract class ApplicationMapper {
    @Autowired
    @Qualifier("apiCandidateCvRepository")
    private CandidateCvRepository candidateCvRepository;
    @Autowired
    @Qualifier("apiJobRepository")
    private JobRepository jobRepository;

    @Mapping(source = "jobId", target = "job")
    @Mapping(source = "cvId", target = "cvFile")
    public abstract Application toApplication(ApplicationCreateRequest request);

    public abstract List<ApplicationSimpleResponse> toApplicationSimpleResponseList(List<Application> applications);
    
    @Mapping(target = "candidate", ignore = true)
    @Mapping(source = "createdAt", target = "appliedAt")
    public abstract ApplicationSimpleResponse toApplicationSimpleResponse(Application application);

    @Mapping(target = "candidate", ignore = true)
    @Mapping(source = "createdAt", target = "appliedAt")
    public abstract ApplicationDetailResponse toApplicationDetailResponse(Application application);

    public abstract List<ReviewResponse> toReviewResponseList(List<ReviewApplication> reviews);

    public abstract ApplicationJobResponse toApplicationJobResponse(Job job);

    protected Job mapJob(Long jobId) {
        if (jobId == null) return null;

        return jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy công việc này"));
    }

    protected CandidateCv mapCandidateCv(Long cvId) {
        if (cvId == null) return null;

        return candidateCvRepository.findById(cvId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cv này"));
    }
}
