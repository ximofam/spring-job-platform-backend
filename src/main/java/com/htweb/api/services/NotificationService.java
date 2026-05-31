package com.htweb.api.services;

import com.htweb.core.helpers.dtos.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getInboxOfUser(Long userId);

    int countUnReadMessageOfUser(Long userId);

    void markAsRead(Long userId, Long notifyId);

    void deleteMsg(Long userId, Long notifyId);
}
