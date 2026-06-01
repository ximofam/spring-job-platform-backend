package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.NotificationRepository;
import com.htweb.core.pojo.Notification;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository("apiNotificationRepository")
public class NotificationRepositoryImpl extends BaseRepositoryImpl<Notification, Long>
        implements NotificationRepository {
    public NotificationRepositoryImpl() {
        super(Notification.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isBelongToUser(Long notifyId, Long userId) {
        String hql = "SELECT count(n) FROM Notification n WHERE n.id = :notifyId AND n.user.id = :userId";

        Long count = getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("notifyId", notifyId)
                .setParameter("userId", userId)
                .uniqueResult();

        return count == null || count <= 0;
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        String hql = "UPDATE Notification n SET n.readAt = :now WHERE n.id = :id";

        getCurrentSession()
                .createMutationQuery(hql)
                .setParameter("now", Instant.now())
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public int countUnReadMsgOfUser(Long userId) {
        String hql = "SELECT count(n) FROM Notification n WHERE n.user.id = :userId AND n.readAt IS NULL";

        Long count = getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("userId", userId)
                .uniqueResult();

        return count != null ? count.intValue() : 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notification> getInboxOfUser(Long userId) {
        String hql = "FROM Notification n WHERE n.user.id = :userId ORDER BY n.id DESC";

        return getCurrentSession()
                .createQuery(hql, Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
