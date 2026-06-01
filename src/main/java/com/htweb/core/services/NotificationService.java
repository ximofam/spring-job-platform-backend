package com.htweb.core.services;

import java.util.Map;

public interface NotificationService {
    Long sendNotify(Long userId, String title, String message, Map<String, Object> extras);

    Long sendNotify(Long userId, String title, String message);
}
