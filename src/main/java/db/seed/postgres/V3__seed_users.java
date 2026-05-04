package db.seed.postgres;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.util.List;

public class V3__seed_users extends BaseJavaMigration {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        List<SeedUser> users = List.of(
                new SeedUser("ximofam", "ximofam@gmail.com", "admin123", "ADMIN", "Pham Dang Quoc Vien"),
                new SeedUser("huy", "huy@gmail.com", "admin123", "ADMIN", "Nguyen Bao Huy")
        );

        insertUsers(context, users);
        assignRoles(context, users);
    }

    private void insertUsers(Context context, List<SeedUser> users) throws Exception {
        String sql = """
                INSERT INTO users (username, email, password_hash, name, avatar_url, user_role)
                SELECT ?, ?, ?, ?, ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM users WHERE username = ?
                )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedUser user : users) {
                stmt.setString(1, user.username());
                stmt.setString(2, user.email());
                stmt.setString(3, encoder.encode(user.password()));
                stmt.setString(4, user.name());
                stmt.setString(5, "https://res.cloudinary.com/datah8lgd/image/upload/v1773747273/images_patu6r.png");
                stmt.setString(6, user.role());
                stmt.setString(7, user.username());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void assignRoles(Context context, List<SeedUser> users) throws Exception {
        String sql = """
                INSERT INTO user_roles (user_id, role_id)
                SELECT u.id, r.id
                FROM users u
                CROSS JOIN roles r
                WHERE u.username = ? 
                  AND r.name = ?
                  AND NOT EXISTS (
                      SELECT 1 FROM user_roles ur 
                      WHERE ur.user_id = u.id AND ur.role_id = r.id
                  )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedUser user : users) {
                stmt.setString(1, user.username());
                stmt.setString(2, user.role());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private record SeedUser(String username, String email, String password, String role, String name) {
    }
}