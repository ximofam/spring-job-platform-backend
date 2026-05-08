package com.htweb.api.services;

import com.htweb.api.dtos.user.*;

public interface UserService {
    UserSimpleResponse getUserById(Long id);

    UserDetailResponse getUserDetailById(Long id);

    UserDetailResponse getUserDetailByUsername(String username);

    void updateUser(Long userId, UserUpdateRequest request);

    void addEducationForCandidate(Long userId, EducationCreateRequest request);

    void addExperienceForCandidate(Long userId, ExperienceCreateRequest request);
}
