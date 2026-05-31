package com.htweb.core.tasks;

import com.htweb.core.enums.JobStatus;
import com.htweb.core.helpers.dtos.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
public class ExpireJobsTask {
    private final SessionFactory factory;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    @Scheduled(fixedRateString = "${app.jobs.expire-interval:1h}")
    @Transactional
    public void execute() {
        Session session = factory.getCurrentSession();

        String hqlFetch = """
                SELECT e.id, j.title, j.id
                FROM Job j
                JOIN EmployerProfile e ON e.company = j.company
                WHERE j.status = :status
                    AND j.expiredAt <= :now
                """;

        List<Object[]> results = session.createQuery(hqlFetch, Object[].class)
                .setParameter("status", JobStatus.PUBLISHED)
                .setParameter("now", Instant.now())
                .getResultList();

        if (!results.isEmpty()) {
            List<Long> jobExpiredIds = results.stream()
                    .map(row -> (Long) row[2])
                    .toList();

            String hqlUpdateJob = """
                    UPDATE Job j
                    SET j.status = :status
                    WHERE j.id IN (:ids)
                    """;

            session.createMutationQuery(hqlUpdateJob)
                    .setParameter("status", JobStatus.EXPIRED)
                    .setParameterList("ids", jobExpiredIds)
                    .executeUpdate();


            for (Object[] row : results) {
                NotificationRequest notificationRequest = getNotificationRequest(row);
                redisTemplate.convertAndSend(notificationTopic.getTopic(), notificationRequest);
            }
        }
    }

    private static NotificationRequest getNotificationRequest(Object[] row) {
        Long employerId = (Long) row[0];
        String jobTitle = (String) row[1];
        Long jobId = (Long) row[2];

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(employerId);
        notificationRequest.setTitle("Tin tuyển dụng của bạn đã hết hạn !!!");
        notificationRequest.setMessage(String.format("Tin tuyển dụng \"%s\" đã hết hạn", jobTitle));
        notificationRequest.setExtras(Map.of("jobId", jobId));
        return notificationRequest;
    }
}
