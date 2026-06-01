package com.htweb.core.services.impl;

import com.htweb.core.helpers.dtos.NotificationRequest;
import com.htweb.core.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    @Override
    public Long sendNotify(Long userId, String title, String message, Map<String, Object> extras) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(userId);
        notificationRequest.setTitle(title);
        notificationRequest.setMessage(message);
        notificationRequest.setExtras(extras);

        return redisTemplate.convertAndSend(notificationTopic.getTopic(), notificationRequest);
    }

    @Override
    public Long sendNotify(Long userId, String title, String message) {
        return sendNotify(userId, title, message, null);
    }
}
