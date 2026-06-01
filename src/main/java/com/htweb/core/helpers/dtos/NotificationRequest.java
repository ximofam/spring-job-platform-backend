package com.htweb.core.helpers.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private Map<String, Object> extras;
    private Instant createdAt = Instant.now();
}
