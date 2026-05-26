package com.htweb.api.services.impl;

import com.htweb.api.dtos.user.EducationCreateRequest;
import com.htweb.api.dtos.user.ExperienceCreateRequest;
import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.api.dtos.user.UserUpdateRequest;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.exceptions.users.UserNotFoundException;
import com.htweb.api.mappers.CandidateProfileMapper;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.repositories.*;
import com.htweb.api.services.UserService;
import com.htweb.core.enums.UserRole;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.pojo.Education;
import com.htweb.core.pojo.Experience;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
                userRes.setProfile(candidateProfileMapper.toCandidateProfileResponse(profile));
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
                userRes.setProfile(candidateProfileMapper.toCandidateProfileResponse(profile));
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
}
