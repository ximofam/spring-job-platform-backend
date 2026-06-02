package com.htweb.core.tasks;

import com.htweb.core.publishers.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindRelatedJobAndSendToCandidateTask {
    private final NotificationPublisher notificationPublisher;
    private final SessionFactory factory;

    @Async
    public void execute(Long jobId) {
        
    }
}
