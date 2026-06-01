package com.htweb.api.mappers;

import com.htweb.api.dtos.application.CandidateCvResponse;
import com.htweb.api.dtos.user.EducationCreateRequest;
import com.htweb.api.dtos.user.EducationResponse;
import com.htweb.api.dtos.user.ExperienceCreateRequest;
import com.htweb.api.dtos.user.ExperienceResponse;
import com.htweb.core.pojo.CandidateCv;
import com.htweb.core.pojo.Education;
import com.htweb.core.pojo.Experience;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {
    Education toEducation(EducationCreateRequest request);

    EducationResponse toEducationResponse(Education education);

    ExperienceResponse toExperienceResponse(Experience experience);

    Experience toExperience(ExperienceCreateRequest request);

    List<CandidateCvResponse> toCandidateCvResponseList(List<CandidateCv> candidateCvs);

    CandidateCvResponse toCandidateCvResponse(CandidateCv candidateCv);
}
