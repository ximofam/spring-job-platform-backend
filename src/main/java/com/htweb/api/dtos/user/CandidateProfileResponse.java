package com.htweb.api.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CandidateProfileResponse {
    private String bio;
    private List<EducationResponse> educations;
    private List<ExperienceResponse> experiences;
}
