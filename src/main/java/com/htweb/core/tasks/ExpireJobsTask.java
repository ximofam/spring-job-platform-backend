package com.htweb.core.tasks;

import com.htweb.core.enums.JobStatus;
import com.htweb.core.publishers.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.PropertySource;
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
    private final NotificationPublisher notificationPublisher;

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
                Long employerId = (Long) row[0];
                String jobTitle = (String) row[1];
                Long jobId = (Long) row[2];

                notificationPublisher.publish(employerId,
                        "Tin tuyển dụng của bạn đã hết hạn !!!",
                        String.format("Tin tuyển dụng \"%s\" đã hết hạn", jobTitle),
                        Map.of("jobId", jobId));
            }
        }
    }

}
