package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.services.NotificationService;
import com.htweb.core.helpers.dtos.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class ApiNotificationController {
    @Qualifier("apiNotificationService")
    private final NotificationService notificationService;

    @GetMapping("/my-inbox")
    public ResponseEntity<List<NotificationResponse>> getMyInbox(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(notificationService.getInboxOfUser(userId));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {

        notificationService.markAsRead(userId, id);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> countUnReadMsg(@AuthenticationPrincipal Long userId) {
        int count = notificationService.countUnReadMessageOfUser(userId);
        ApiResponse res = new ApiResponse();
        res.setData(Map.of("unreadCount", count));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMsg(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        notificationService.deleteMsg(userId, id);

        return ResponseEntity.noContent().build();
    }
}
