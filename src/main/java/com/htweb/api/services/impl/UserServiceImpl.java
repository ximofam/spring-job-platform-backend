package com.htweb.api.services.impl;

import com.htweb.api.dtos.application.CandidateCvResponse;
import com.htweb.api.dtos.user.*;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.exceptions.users.UserNotFoundException;
import com.htweb.api.mappers.CandidateProfileMapper;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.repositories.*;
import com.htweb.api.services.UserService;
import com.htweb.core.enums.UserRole;
import com.htweb.core.pojo.*;
import com.htweb.core.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service("apiUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("apiUserRepository")
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Qualifier("apiCandidateProfileRepository")
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateProfileMapper candidateProfileMapper;
    @Qualifier("apiEmployerProfileRepository")
    private final EmployerProfileRepository employerProfileRepository;
    @Qualifier("apiEducationRepository")
    private final EducationRepository educationRepository;
    @Qualifier("apiExperienceRepository")
    private final ExperienceRepository experienceRepository;
    private final StorageService storageService;
    @Qualifier("apiCandidateCvRepository")
    private final CandidateCvRepository candidateCvRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetailById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        UserDetailResponse userRes = userMapper.toUserDetailResponse(user);
        if (user.getUserRole().equals(UserRole.EMPLOYER)) {
            employerProfileRepository.findById(id).ifPresent(profile -> {
                userRes.setProfile(userMapper.toEmployerProfileResponse(profile));
            });
        } else if (user.getUserRole().equals(UserRole.CANDIDATE)) {
            candidateProfileRepository.findById(id).ifPresent(profile -> {
                userRes.setProfile(userMapper.toCandidateProfileResponse(profile));
            });
        }

        return userRes;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetailByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        UserDetailResponse userRes = userMapper.toUserDetailResponse(user);
        if (user.getUserRole().equals(UserRole.EMPLOYER)) {
            employerProfileRepository.findById(user.getId()).ifPresent(profile -> {
                userRes.setProfile(userMapper.toEmployerProfileResponse(profile));
            });
        } else if (user.getUserRole().equals(UserRole.CANDIDATE)) {
            candidateProfileRepository.findById(user.getId()).ifPresent(profile -> {
                userRes.setProfile(userMapper.toCandidateProfileResponse(profile));
            });
        }

        return userRes;
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: %d", userId));

        userMapper.updateUser(request, user);

        userRepository.update(user);
    }

    @Override
    @Transactional
    public void addEducationForCandidate(Long userId, EducationCreateRequest request) {
        CandidateProfile profile = candidateProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found candidate profile of user id: %d", userId));

        Education education = candidateProfileMapper.toEducation(request);
        education.setCandidateProfile(profile);

        educationRepository.save(education);
    }

    @Override
    @Transactional
    public void addExperienceForCandidate(Long userId, ExperienceCreateRequest request) {
        CandidateProfile profile = candidateProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found candidate profile of user id: %d", userId));

        Experience experience = candidateProfileMapper.toExperience(request);
        experience.setCandidateProfile(profile);

        experienceRepository.save(experience);
    }

    @Override
    @Transactional
    public String uploadAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        try {
            String avatarUrl = storageService.uploadImage(file);
            user.setAvatarUrl(avatarUrl);
            userRepository.update(user);

            return avatarUrl;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi trong quá trình upload ảnh đại diện: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String uploadCV(Long userId, UserCVUploadRequest request) {
        CandidateProfile candidateProfile = candidateProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ứng viên này"));

        try {
            String cvUrl = storageService.uploadPdf(request.getFile(), "candidate/cv/");
            CandidateCv candidateCv = new CandidateCv();
            candidateCv.setTitle(request.getTitle());
            candidateCv.setFileUrl(cvUrl);
            candidateCv.setCandidateProfile(candidateProfile);
            candidateCvRepository.save(candidateCv);

            return cvUrl;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi trong quá trình upload cv: " + e.getMessage());
        }

    }

    @Override
    public List<CandidateCvResponse> getCandidateCVs(Long userId) {
        List<CandidateCv> candidateCvs = candidateCvRepository.findByUserId(userId);
        if (candidateCvs == null || candidateCvs.isEmpty()) {
            return List.of();
        }

        return candidateProfileMapper.toCandidateCvResponseList(candidateCvs);
    }
}
