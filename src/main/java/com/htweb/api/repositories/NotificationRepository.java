package com.htweb.api.repositories;

import com.htweb.core.pojo.Notification;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface NotificationRepository extends BaseRepository<Notification, Long> {
    boolean isBelongToUser(Long notifyId, Long userId);

    void markAsRead(Long id);

    int countUnReadMsgOfUser(Long userId);

    List<Notification> getInboxOfUser(Long userId);
}
