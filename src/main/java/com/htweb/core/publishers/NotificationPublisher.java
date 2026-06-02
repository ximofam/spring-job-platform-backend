package com.htweb.core.publishers;

import com.htweb.core.helpers.dtos.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("notificationTopic")
    private final ChannelTopic notificationTopic;

    public Long publish(Long userId, String title, String message, Map<String, Object> extras) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(userId);
        notificationRequest.setTitle(title);
        notificationRequest.setMessage(message);
        notificationRequest.setExtras(extras);

        return redisTemplate.convertAndSend(notificationTopic.getTopic(), notificationRequest);
    }

    public Long publish(Long userId, String title, String message) {
        return publish(userId, title, message, null);
    }
}
