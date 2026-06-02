package com.htweb.api.services.impl;

import com.htweb.api.dtos.application.ApplicationCreateRequest;
import com.htweb.api.dtos.application.ApplicationDetailResponse;
import com.htweb.api.dtos.application.ApplicationReviewRequest;
import com.htweb.api.dtos.application.ApplicationSimpleResponse;
import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.ForbiddenException;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.mappers.ApplicationMapper;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.repositories.*;
import com.htweb.api.services.ApplicationService;
import com.htweb.core.enums.ApplicationStatus;
import com.htweb.core.enums.UserRole;
import com.htweb.core.pojo.Application;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.pojo.ReviewApplication;
import com.htweb.core.pojo.User;
import com.htweb.core.publishers.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("apiApplicationService")
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    @Qualifier("apiApplicationRepository")
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    @Qualifier("apiCandidateProfileRepository")
    private final CandidateProfileRepository candidateProfileRepository;
    @Qualifier("apiReviewApplicationRepository")
    private final ReviewApplicationRepository reviewApplicationRepository;
    @Qualifier("apiUserRepository")
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final NotificationPublisher notificationPublisher;
    @Qualifier("apiEmployerProfileRepository")
    private final EmployerProfileRepository employerProfileRepository;

    @Override
    @Transactional
    public ApplicationSimpleResponse createApplication(Long userId, ApplicationCreateRequest request) {
        CandidateProfile candidateProfile = candidateProfileRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("Bạn không phải ứng viên"));

        if (applicationRepository.existsByJobIdAndCandidateId(request.getJobId(), userId)) {
            throw new BadRequestException("Bạn đã nộp đơn cho job này rồi");
        }

        Application application = applicationMapper.toApplication(request);
        application.setCandidateProfile(candidateProfile);
        applicationRepository.save(application);

        employerProfileRepository.findByJobId(application.getJobId())
                .ifPresent(employerProfile ->
                        notificationPublisher.publish(
                                employerProfile.getId(),
                                "Có đơn ứng tuyển !!!!!",
                                String.format("%s đã nộp hồ sơ vào tin \"%s\" của bạn",
                                        candidateProfile.getUser().getName(),
                                        application.getJob().getTitle())
                        ));

        return applicationMapper.toApplicationSimpleResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationSimpleResponse> getApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User với id là %d không tồn tại", userId));

        List<ApplicationSimpleResponse> res = null;
        if (user.getUserRole().equals(UserRole.CANDIDATE)) {
            res = applicationMapper.toApplicationSimpleResponseList(applicationRepository.findApplicationOfCandidate(userId));
        } else if (user.getUserRole().equals(UserRole.EMPLOYER)) {
            List<Application> applications = applicationRepository.findApplicationOfEmployer(userId);

            res = applications.stream().map(app -> {
                ApplicationSimpleResponse response = applicationMapper.toApplicationSimpleResponse(app);
                if (app.getCandidateProfile() != null && app.getCandidateProfile().getUser() != null) {
                    User candidate = app.getCandidateProfile().getUser();
                    response.setCandidate(userMapper.toUserSimpleResponse(candidate));
                }

                return response;
            }).toList();
        }

        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getDetailApplication(Long userId, Long applicationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User với id là %d không tồn tại", userId));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Không tồn tại hồ sơ này"));

        if (!applicationRepository.isRelateToUser(applicationId, userId)) {
            throw new ForbiddenException("Bạn không được quyền truy cập vào hồ sơ ứng tuyển này");
        }

        ApplicationDetailResponse res = applicationMapper.toApplicationDetailResponse(application);

        if (user.getUserRole().equals(UserRole.EMPLOYER)) {
            CandidateProfile candidateProfile = application.getCandidateProfile();
            UserDetailResponse userDetailResponse = userMapper.toUserDetailResponse(candidateProfile.getUser());
            userDetailResponse.setProfile(userMapper.toCandidateProfileResponse(candidateProfile));

            res.setCandidate(userDetailResponse);
        }

        return res;
    }

    @Override
    @Transactional
    public void reviewApplication(Long userId, Long applicationId, ApplicationReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User với id là %d không tồn tại", userId));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Không tồn tại hồ sơ này"));

        if (!applicationRepository.isRelateToUser(applicationId, userId)) {
            throw new ForbiddenException("Bạn không được quyền truy cập vào hồ sơ ứng tuyển này");
        }

        ReviewApplication reviewApplication = new ReviewApplication();
        reviewApplication.setUser(user);
        reviewApplication.setOldStatus(application.getStatus());
        reviewApplication.setNewStatus(request.getStatus());
        reviewApplication.setReason(request.getReason());
        reviewApplication.setApplication(application);

        application.setStatus(request.getStatus());
        applicationRepository.update(application);
        reviewApplicationRepository.save(reviewApplication);

        String message = String.format("Hồ sơ cho công việc \"%s\" của bạn ", application.getJob().getTitle());
        if (reviewApplication.getNewStatus().equals(ApplicationStatus.ACCEPTED)) {
            message += "đã được duyệt";
        } else if (reviewApplication.getNewStatus().equals(ApplicationStatus.REJECTED)) {
            message += "đã bị từ chối";
        } else {
            message = "đã được đánh giá";
        }

        notificationPublisher.publish(
                application.getCandidateProfile().getId(),
                "Nhà tuyển dụng đã đánh giá hồ sơ của bạn",
                message
        );
    }
}
