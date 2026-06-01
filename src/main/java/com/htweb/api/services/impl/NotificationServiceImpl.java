package com.htweb.api.services.impl;

import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.repositories.NotificationRepository;
import com.htweb.api.services.NotificationService;
import com.htweb.core.helpers.dtos.NotificationResponse;
import com.htweb.core.helpers.mappers.NotificationMapper;
import com.htweb.core.pojo.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("apiNotificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Qualifier("apiNotificationRepository")
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getInboxOfUser(Long userId) {
        List<Notification> notifications = notificationRepository.getInboxOfUser(userId);
        return notificationMapper.toNotificationResponseList(notifications);
    }

    @Override
    public int countUnReadMessageOfUser(Long userId) {
        return notificationRepository.countUnReadMsgOfUser(userId);
    }

    @Override
    public void markAsRead(Long userId, Long notifyId) {
        if (notificationRepository.isBelongToUser(notifyId, userId)) {
            throw new BadRequestException("Không tồn tại tin nhắn này");
        }

        notificationRepository.markAsRead(notifyId);
    }

    @Override
    @Transactional
    public void deleteMsg(Long userId, Long notifyId) {
        if (notificationRepository.isBelongToUser(notifyId, userId)) {
            throw new BadRequestException("Không tồn tại tin nhắn này");
        }

        notificationRepository.delete(notifyId);
    }


}
