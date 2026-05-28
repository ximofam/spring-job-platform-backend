package com.htweb.api.services;

import com.htweb.api.dtos.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDetailResponse getUserDetailById(Long id);

    UserDetailResponse getUserDetailByUsername(String username);

    void updateUser(Long userId, UserUpdateRequest request);

    void addEducationForCandidate(Long userId, EducationCreateRequest request);

    void addExperienceForCandidate(Long userId, ExperienceCreateRequest request);

    String uploadAvatar(Long userId, MultipartFile file);

    String uploadCV(Long userId, UserCVUploadRequest request);

    List<CandidateCvResponse> getCandidateCVs(Long userId);
}
