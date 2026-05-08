package com.htweb.api.mappers;

import com.htweb.api.dtos.user.*;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.pojo.Education;
import com.htweb.core.pojo.Experience;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {
    Education toEducation(EducationCreateRequest request);

    EducationResponse toEducationResponse(Education education);

    CandidateProfileResponse toCandidateProfileResponse(CandidateProfile profile);

    ExperienceResponse toExperienceResponse(Experience experience);

    Experience toExperience(ExperienceCreateRequest request);
}
