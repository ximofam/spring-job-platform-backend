package com.htweb.core.tasks;

import com.htweb.core.pojo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {
    private final SessionFactory factory;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void cleanupExpiredTokens() {
        System.out.println("Bắt đầu dọn dẹp Refresh Tokens...");

        int batchSize = 1000;
        int totalDeleted = 0;
        int deletedInCurrentBatch = 0;

        try (Session session = factory.openSession()) {
            do {
                Transaction tx = session.beginTransaction();
                try {
                    String sql = """
                            DELETE FROM refresh_tokens
                            WHERE id IN (
                                SELECT id FROM refresh_tokens
                                WHERE is_revoked = true OR expires_at <= :now
                                LIMIT :batchSize
                            )
                            """;

                    deletedInCurrentBatch = session.createNativeQuery(sql, RefreshToken.class)
                            .setParameter("now", Instant.now())
                            .setParameter("batchSize", batchSize)
                            .executeUpdate();


                    tx.commit();
                    totalDeleted += deletedInCurrentBatch;

                } catch (Exception e) {
                    if (tx != null && tx.isActive()) {
                        tx.rollback();
                    }
                    System.err.println("Lỗi khi thực thi xóa dữ liệu: " + e.getMessage());
                    break;
                }


                if (deletedInCurrentBatch == batchSize) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            } while (deletedInCurrentBatch == batchSize);

            System.out.println("Hoàn tất dọn dẹp! Đã xóa tổng cộng: " + totalDeleted + " tokens.");
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo Session: " + e.getMessage());
        }
    }
}
