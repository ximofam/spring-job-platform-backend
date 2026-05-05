package db.seed.postgres;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.util.List;

public class V4__seed_users extends BaseJavaMigration {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        List<SeedUser> users = List.of(
                new SeedUser("ximofam", "ximofam@gmail.com", "admin123", "ADMIN", "Pham Dang Quoc Vien"),
                new SeedUser("huy", "huy@gmail.com", "admin123", "ADMIN", "Nguyen Bao Huy"),
                new SeedUser("candidate01", "candidate01@gmail.com", "candidate123", "CANDIDATE", "Nguyen Van An"),
                new SeedUser("employer01", "employer01@gmail.com", "employer123", "EMPLOYER", "Tran Thi Bich")
        );

        insertUsers(context, users);
        assignRoles(context, users);
        seedCandidateProfile(context);
        seedCompanyAndEmployerProfile(context);
    }

    private void insertUsers(Context context, List<SeedUser> users) throws Exception {
        String sql = """
                INSERT INTO users (username, email, password_hash, name, avatar_url, user_role, gender, date_of_birth, address)
                SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?
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
                stmt.setString(7, user.gender());
                stmt.setObject(8, user.dob() != null ? java.sql.Date.valueOf(user.dob()) : null);
                stmt.setString(9, user.address());
                stmt.setString(10, user.username());
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

    private void seedCandidateProfile(Context context) throws Exception {
        // Insert candidate_profile
        String profileSql = """
                INSERT INTO candidate_profiles (user_id, bio, created_at, updated_at)
                SELECT u.id,
                       'Fresher Java Backend developer, passionate about building scalable systems.',
                       NOW(), NOW()
                FROM users u
                WHERE u.username = 'candidate01'
                  AND NOT EXISTS (
                      SELECT 1 FROM candidate_profiles cp WHERE cp.user_id = u.id
                  )
                """;
        context.getConnection().prepareStatement(profileSql).executeUpdate();

        // Insert educations
        String eduSql = """
                INSERT INTO educations (candidate_profile_id, school, major, degree, start_date, end_date, description, created_at, updated_at)
                SELECT u.id,
                       'Ho Chi Minh City Open University',
                       'Software Engineering',
                       'BACHELOR',
                       '2019-09-01',
                       '2023-06-30',
                       'Graduated with GPA 3.4/4.0, focused on distributed systems.',
                       NOW(), NOW()
                FROM users u
                WHERE u.username = 'candidate01'
                  AND NOT EXISTS (
                      SELECT 1 FROM educations e WHERE e.candidate_profile_id = u.id
                  )
                """;
        context.getConnection().prepareStatement(eduSql).executeUpdate();

        // Insert experiences
        String expSql = """
                INSERT INTO experiences (candidate_profile_id, company_name, position, start_date, end_date, description, created_at, updated_at)
                SELECT u.id,
                       'FPT Software',
                       'Java Backend Intern',
                       '2023-07-01',
                       '2023-12-31',
                       'Worked on REST APIs using Spring Boot, participated in Agile sprints.',
                       NOW(), NOW()
                FROM users u
                WHERE u.username = 'candidate01'
                  AND NOT EXISTS (
                      SELECT 1 FROM experiences e WHERE e.candidate_profile_id = u.id
                  )
                """;
        context.getConnection().prepareStatement(expSql).executeUpdate();
    }

    private void seedCompanyAndEmployerProfile(Context context) throws Exception {
        // Insert company
        String companySql = """
                INSERT INTO companies (name, type, employee_size, description, tax_code, created_at, updated_at)
                SELECT 'TechCorp Vietnam',
                       'PRODUCT',
                       'FROM_100_TO_499',
                       'A product-focused tech company building SaaS solutions for SEA market.',
                       '0123456789',
                       NOW(), NOW()
                WHERE NOT EXISTS (
                    SELECT 1 FROM companies WHERE tax_code = '0123456789'
                )
                """;
        context.getConnection().prepareStatement(companySql).executeUpdate();

        // Insert employer_profile
        String employerSql = """
                INSERT INTO employer_profiles (user_id, company_id, status, created_at, updated_at)
                SELECT u.id,
                       c.id,
                       'APPROVED',
                       NOW(), NOW()
                FROM users u
                CROSS JOIN companies c
                WHERE u.username = 'employer01'
                  AND c.tax_code = '0123456789'
                  AND NOT EXISTS (
                      SELECT 1 FROM employer_profiles ep WHERE ep.user_id = u.id
                  )
                """;
        context.getConnection().prepareStatement(employerSql).executeUpdate();
    }

    private record SeedUser(
            String username,
            String email,
            String password,
            String role,
            String name,
            String gender,
            String dob,
            String address
    ) {
        // Constructor overload cho admin (không cần gender/dob/address)
        SeedUser(String username, String email, String password, String role, String name) {
            this(username, email, password, role, name, null, null, null);
        }
    }
}