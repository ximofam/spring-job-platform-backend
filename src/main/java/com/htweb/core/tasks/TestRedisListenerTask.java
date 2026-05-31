package com.htweb.core.tasks;

import com.htweb.core.helpers.dtos.NotificationRequest;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TestRedisListenerTask {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic notificationTopic;
    @Autowired
    private SessionFactory factory;

    @Scheduled(fixedRate = 60000)
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void execute() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String testMessage = "Đây là tin nhắn tự động của hệ thống gửi cho toàn bộ user.\n"
                + "Được gửi vào lúc: " + currentTime;

        String userIdsHql = "SELECT u.id FROM User u";
        List<Long> userIds = factory.getCurrentSession().createQuery(userIdsHql, Long.class)
                .getResultList();

        List<NotificationRequest> notificationRequests = userIds.stream()
                .map(userId -> {
                    NotificationRequest notificationDto = new NotificationRequest();
                    notificationDto.setUserId(userId);
                    notificationDto.setTitle("Tin nhắn từ hệ thống");
                    notificationDto.setMessage(testMessage);
                    return notificationDto;
                }).toList();


        for (NotificationRequest request : notificationRequests) {
            redisTemplate.convertAndSend(notificationTopic.getTopic(), request);
        }
    }
}
