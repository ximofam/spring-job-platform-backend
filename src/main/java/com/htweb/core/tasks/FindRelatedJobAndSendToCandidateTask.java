package com.htweb.core.tasks;

import com.htweb.core.pojo.Job;
import com.htweb.core.publishers.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindRelatedJobAndSendToCandidateTask {
    private final NotificationPublisher notificationPublisher;
    private final SessionFactory factory;

    @Async
    public void execute(Long newJobId) {
        try (Session session = factory.openSession()) {
            Job newJob = session.get(Job.class, newJobId);
            if (newJob == null) {
                log.warn("Không tìm thấy Job mới với ID: {}", newJobId);
                return;
            }
            String newJobTitle = newJob.getTitle();

            String nativeSimilarJobIdsSql = """
                        WITH TargetJob AS (
                            SELECT embedding FROM jobs WHERE id = :targetJobId
                        )
                        SELECT j.id
                        FROM jobs j
                        WHERE j.id != :targetJobId
                          AND (j.embedding <=> (SELECT embedding FROM TargetJob)) <= 0.2
                    """;

            NativeQuery<Long> nativeQuery = session.createNativeQuery(nativeSimilarJobIdsSql, Long.class);
            nativeQuery.setParameter("targetJobId", newJobId);
            List<Long> similarJobIds = nativeQuery.list();

            if (similarJobIds == null || similarJobIds.isEmpty()) {
                log.info("Không có job nào tương đồng với Job mới (ID: {}). Bỏ qua gửi thông báo.", newJobId);
                return;
            }

            String hql = """
                        SELECT DISTINCT a.candidateProfile.id
                        FROM Application a
                        WHERE a.job.id IN :jobIds
                    """;

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameterList("jobIds", similarJobIds);
            List<Long> candidateIds = query.list();

            if (candidateIds.isEmpty()) {
                log.info("Không có ứng viên nào ứng tuyển các job liên quan.");
                return;
            }

            String notifTitle = "Có công việc mới phù hợp với bạn!";
            String notifMessage = String.format("Công việc \"%s\"", newJobTitle);

            for (Long candidateId : candidateIds) {
                notificationPublisher.publish(candidateId, notifTitle, notifMessage);
            }
            log.info("Đã gửi thông báo job mới (ID: {}) thành công cho {} ứng viên.", newJobId, candidateIds.size());
        } catch (Exception e) {
            log.error("Lỗi hệ thống khi chạy task FindRelatedJobAndSendToCandidateTask cho Job ID {}: {}", newJobId, e.getMessage(), e);
        }
    }
}
