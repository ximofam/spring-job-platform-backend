package com.htweb.core.tasks;

import com.htweb.core.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpireJobsTask {
    private final SessionFactory factory;

    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void execute() {
        Session session = factory.getCurrentSession();

        String hqlJobExpired = """
                SELECT j.id FROM Job j
                WHERE j.status = :status
                    AND j.expiredAt <= :now
                """;

        List<Long> jobExpiredIds = session.createQuery(hqlJobExpired, Long.class)
                .setParameter("status", JobStatus.PUBLISHED)
                .setParameter("now", Instant.now())
                .getResultList();

        if (!jobExpiredIds.isEmpty()) {
            String hqlUpdateJob = """
                    UPDATE Job j
                    SET j.status = :status
                    WHERE j.id IN (:ids)
                    """;

            session.createMutationQuery(hqlUpdateJob)
                    .setParameter("status", JobStatus.EXPIRED)
                    .setParameterList("ids", jobExpiredIds)
                    .executeUpdate();
        }

    }
}
