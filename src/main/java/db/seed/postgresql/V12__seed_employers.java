package db.seed.postgresql;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.util.List;

public class V12__seed_employers extends BaseJavaMigration {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
        List<SeedEmployer> employers = List.of(
                new SeedEmployer(
                        "employer02", "employer02@gmail.com", "Le Minh Khoa",
                        "FPT Software", "fpt-software", "OUTSOURCE", "ENTERPRISE",
                        "One of Vietnam's largest IT outsourcing companies serving global clients.",
                        "0100686209"
                ),
                new SeedEmployer(
                        "employer03", "employer03@gmail.com", "Pham Thi Lan",
                        "VNG Corporation", "vng-corporation", "PRODUCT", "ENTERPRISE",
                        "Leading Vietnamese internet company behind Zalo and ZaloPay platforms.",
                        "0303578249"
                ),
                new SeedEmployer(
                        "employer04", "employer04@gmail.com", "Nguyen Quoc Hung",
                        "KMS Technology", "kms-technology", "OUTSOURCE", "LARGE",
                        "Vietnam-based software development firm specializing in Agile delivery.",
                        "0311628349"
                ),
                new SeedEmployer(
                        "employer05", "employer05@gmail.com", "Hoang Thi Mai",
                        "Nashtech Vietnam", "nashtech-vietnam", "OUTSOURCE", "LARGE",
                        "Global technology solutions provider with strong presence in Vietnam.",
                        "0312750691"
                ),
                new SeedEmployer(
                        "employer06", "employer06@gmail.com", "Vo Van Thanh",
                        "Tiki Corporation", "tiki-corporation", "PRODUCT", "LARGE",
                        "Vietnam's leading e-commerce platform offering fast delivery and trusted service.",
                        "0309532909"
                )
        );

        insertUsers(context, employers);
        assignRoles(context, employers);
        insertCompanies(context, employers);
        insertEmployerProfiles(context, employers);
    }

    private void insertUsers(Context context, List<SeedEmployer> employers) throws Exception {
        String sql = """
                INSERT INTO users (username, email, password_hash, name, avatar_url, user_role, country_id)
                SELECT ?, ?, ?, ?, ?, 'EMPLOYER',
                       (SELECT id FROM countries WHERE code = 'VN' LIMIT 1)
                WHERE NOT EXISTS (
                    SELECT 1 FROM users WHERE username = ?
                )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedEmployer e : employers) {
                stmt.setString(1, e.username());
                stmt.setString(2, e.email());
                stmt.setString(3, encoder.encode("employer123"));
                stmt.setString(4, e.name());
                stmt.setString(5, "https://res.cloudinary.com/datah8lgd/image/upload/v1773747273/images_patu6r.png");
                stmt.setString(6, e.username());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void assignRoles(Context context, List<SeedEmployer> employers) throws Exception {
        String sql = """
                INSERT INTO user_roles (user_id, ole_id)
                SELECT u.id, r.id
                FROM users u
                CROSS JOIN roles r
                WHERE u.username = ?
                  AND r.name = 'EMPLOYER'
                  AND NOT EXISTS (
                      SELECT 1 FROM user_roles ur
                      WHERE ur.user_id = u.id AND ur.role_id = r.id
                  )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedEmployer e : employers) {
                stmt.setString(1, e.username());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertCompanies(Context context, List<SeedEmployer> employers) throws Exception {
        String sql = """
                INSERT INTO companies (status, name, slug, type, employee_size, description, tax_code, country_id, created_at, updated_at)
                SELECT 'APPROVED', ?, ?, ?, ?, ?, ?,
                       (SELECT id FROM countries WHERE code = 'VN' LIMIT 1), NOW(), NOW()
                WHERE NOT EXISTS (
                    SELECT 1 FROM companies WHERE tax_code = ?
                )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedEmployer e : employers) {
                stmt.setString(1, e.companyName());
                stmt.setString(2, e.slug());
                stmt.setString(3, e.type());
                stmt.setString(4, e.size());
                stmt.setString(5, e.description());
                stmt.setString(6, e.taxCode());
                stmt.setString(7, e.taxCode());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertEmployerProfiles(Context context, List<SeedEmployer> employers) throws Exception {
        String sql = """
                INSERT INTO employer_profiles (user_id, company_id, created_at, updated_at)
                SELECT u.id, c.id, NOW(), NOW()
                FROM users u
                CROSS JOIN companies c
                WHERE u.username = ?
                  AND c.tax_code = ?
                  AND NOT EXISTS (
                      SELECT 1 FROM employer_profiles ep WHERE ep.user_id = u.id
                  )
                """;

        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (SeedEmployer e : employers) {
                stmt.setString(1, e.username());
                stmt.setString(2, e.taxCode());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private record SeedEmployer(
            String username,
            String email,
            String name,
            String companyName,
            String slug,
            String type,
            String size,
            String description,
            String taxCode
    ) {}
}