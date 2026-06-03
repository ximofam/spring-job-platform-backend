package com.htweb.core.tasks;

import com.htweb.core.helpers.dtos.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendMessageToUsersTask {
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void execute(List<Long> userIds, MessageResponse messageResponse) {
        for (Long userId : userIds) {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/messages",
                    messageResponse
            );
        }
    }
}
