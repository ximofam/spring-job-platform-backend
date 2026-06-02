package com.htweb.core.subscribers;

import com.htweb.core.helpers.dtos.JobPostRequest;
import com.htweb.core.services.EmbedService;
import com.htweb.core.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JobPostSubscriber {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionFactory factory;
    private final EmbedService embedService;

    private static final String sqlJobFullText = """
            SELECT
                   'Tiêu đề công việc: ' || COALESCE(title, '') || E'\\n' ||
                   'Mô tả công việc: ' || COALESCE(description, '') || E'\\n' ||
                   'Yêu cầu ứng viên: ' || COALESCE(requirements, '') || E'\\n' ||
                   'Phúc lợi và đãi ngộ: ' || COALESCE(benefit, '') AS full_text
            FROM jobs
            WHERE id = :id
            """;

    private static final String sqlUpdateVector = "UPDATE jobs SET embedding = CAST(:vector AS vector) WHERE id = :id";

    public void onMessage(Object message, String channel) {
        if (!(message instanceof JobPostRequest request)) {
            System.out.println("Tin nhắn không đúng định dạng JobPostRequest: " + message);
            return;
        }

        try (Session session = factory.openSession()) {
            String jobFullText = session.createNativeQuery(sqlJobFullText, String.class)
                    .setParameter("id", request.getId())
                    .getSingleResult();

            float[] vector = embedService.getEmbedding(jobFullText);
            if (vector == null || vector.length == 0) {
                System.err.println("Không thể tạo vector cho Job ID: " + request.getId());
                redisTemplate.opsForList().rightPush(Utils.JOB_POST_RETRY_QUEUE, request.getId());
                return;
            }

            String vectorString = Arrays.toString(vector);

            Transaction tx = session.beginTransaction();
            try {
                int updatedRows = session.createNativeMutationQuery(sqlUpdateVector)
                        .setParameter("vector", vectorString)
                        .setParameter("id", request.getId())
                        .executeUpdate();

                tx.commit();
                System.out.println("Đã cập nhật vector thành công cho Job ID: " + request.getId() + " (" + updatedRows + " dòng)");

            } catch (Exception e) {
                tx.rollback();
                System.err.println("Lỗi khi lưu vector vào DB cho Job ID: " + request.getId());
                redisTemplate.opsForList().rightPush(Utils.JOB_POST_RETRY_QUEUE, request.getId());
            }
        }
    }
}
