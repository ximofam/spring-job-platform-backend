package com.htweb.api.services;

import com.htweb.api.dtos.application.ApplicationCreateRequest;
import com.htweb.api.dtos.application.ApplicationDetailResponse;
import com.htweb.api.dtos.application.ApplicationReviewRequest;
import com.htweb.api.dtos.application.ApplicationSimpleResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationSimpleResponse createApplication(Long userId, ApplicationCreateRequest request);

    List<ApplicationSimpleResponse> getApplications(Long userId);

    ApplicationDetailResponse getDetailApplication(Long userId, Long applicationId);

    void reviewApplication(Long userId, Long applicationId, ApplicationReviewRequest request);
}
