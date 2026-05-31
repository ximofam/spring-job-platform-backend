package com.htweb.core.subscribers;

import com.htweb.core.helpers.dtos.NotificationRequest;
import com.htweb.core.helpers.dtos.NotificationResponse;
import com.htweb.core.helpers.mappers.NotificationMapper;
import com.htweb.core.pojo.Notification;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationSubscriber {
    private final SessionFactory factory;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;


    public void onMessage(Object message, String channel) {
        if (!(message instanceof NotificationRequest request)) {
            System.out.println("Tin nhắn không đúng định dạng NotificationDto: " + message);
            return;
        }

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Notification notification = notificationMapper.toNotification(request);
                User userProxy = session.getReference(User.class, request.getUserId());
                notification.setUser(userProxy);
                session.persist(notification);

                tx.commit();

                NotificationResponse res = notificationMapper.toNotificationResponse(notification);
                messagingTemplate.convertAndSendToUser(
                        request.getUserId().toString(),
                        "/queue/notifications",
                        res
                );

            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }
}
