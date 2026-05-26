package com.htweb.api.services;

import com.htweb.api.dtos.user.EducationCreateRequest;
import com.htweb.api.dtos.user.ExperienceCreateRequest;
import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.api.dtos.user.UserUpdateRequest;

public interface UserService {

    UserDetailResponse getUserDetailById(Long id);

    UserDetailResponse getUserDetailByUsername(String username);

    void updateUser(Long userId, UserUpdateRequest request);

    void addEducationForCandidate(Long userId, EducationCreateRequest request);

    void addExperienceForCandidate(Long userId, ExperienceCreateRequest request);
}
