package com.htweb.core.tasks;

import com.htweb.core.publishers.JobPostPublisher;
import com.htweb.core.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetryFailedJobsScheduler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JobPostPublisher jobPostPublisher;

    @Scheduled(fixedDelay = 300000)
    public void execute() {
        Long currentQueueSize = redisTemplate.opsForList().size(Utils.JOB_POST_RETRY_QUEUE);
        if (currentQueueSize == null || currentQueueSize == 0) {
            return;
        }

        for (int i = 0; i < currentQueueSize; i++) {
            Object poppedValue = redisTemplate.opsForList().leftPop(Utils.JOB_POST_RETRY_QUEUE);
            if (poppedValue == null) {
                continue;
            }

            long failedJobId;
            if (poppedValue instanceof Number) {
                failedJobId = ((Number) poppedValue).longValue();
            } else {
                failedJobId = Long.parseLong(poppedValue.toString());
            }

            jobPostPublisher.publish(failedJobId);
        }
    }
}
