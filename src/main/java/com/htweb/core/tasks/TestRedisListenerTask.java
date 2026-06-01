package com.htweb.core.tasks;

import com.htweb.core.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestRedisListenerTask {
    private final SessionFactory factory;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 600000)
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void execute() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String testMessage = "Đây là tin nhắn tự động của hệ thống gửi cho toàn bộ user.\n"
                + "Được gửi vào lúc: " + currentTime;

        List<Long> userIds = factory.getCurrentSession()
                .createQuery("SELECT u.id FROM User u", Long.class)
                .getResultList();

        userIds.forEach(userId -> {
            notificationService.sendNotify(userId, "Tin nhắn từ hệ thống", testMessage);
        });
    }
}
