package com.htweb.core.publishers;

import com.htweb.core.helpers.dtos.JobPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobPostPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("jobPostTopic")
    private final ChannelTopic jobPostTopic;

    public void publish(Long jobId) {
        JobPostRequest request = new JobPostRequest();
        request.setId(jobId);
        redisTemplate.convertAndSend(jobPostTopic.getTopic(), request);
    }
}
