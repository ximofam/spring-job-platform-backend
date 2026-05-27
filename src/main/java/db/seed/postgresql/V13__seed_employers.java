package db.seed.postgresql;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.time.OffsetDateTime;

public class V13__seed_employers extends BaseJavaMigration {

    private static final String TYPE_OUTSOURCE = "OUTSOURCE";
    private static final String TYPE_PRODUCT = "PRODUCT";

    private static final String SIZE_ENTERPRISE = "ENTERPRISE";
    private static final String SIZE_VERY_LARGE = "VERY_LARGE";
    private static final String SIZE_LARGE = "LARGE";
    private static final String SIZE_MEDIUM = "MEDIUM";

    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_DENIED = "DENIED";

    private static final String COMPANY_APPROVED = "APPROVED";

    private static final String COUNTRY_VN = "VN";

    private static final Object[][] COMPANIES = {
            {"FPT Software", TYPE_OUTSOURCE, SIZE_ENTERPRISE, "0101248141",
                    "FPT Software là công ty công nghệ hàng đầu Việt Nam, cung cấp dịch vụ gia công phần mềm toàn cầu.",
                    "Hà Nội", "Cầu Giấy", "Tòa nhà FPT, 17 Duy Tân"},

            {"VNG Corporation", TYPE_PRODUCT, SIZE_VERY_LARGE, "0303456789",
                    "VNG là công ty công nghệ tiên phong tại Việt Nam với các sản phẩm nổi tiếng như Zalo, ZaloPay.",
                    "Hồ Chí Minh", "Hiệp Bình", "Vạn Phúc City, 1 Đường số 8, Phường Hiệp Bình Phước"},

            {"Tiki Corporation", TYPE_PRODUCT, SIZE_LARGE, "0312345678",
                    "Tiki là nền tảng thương mại điện tử hàng đầu Việt Nam, cam kết mang lại trải nghiệm mua sắm tốt nhất.",
                    "Hồ Chí Minh", "Tân Bình", "52 Út Tịch, Phường 4"},

            {"Axon Active Vietnam", TYPE_OUTSOURCE, SIZE_MEDIUM, "0400112233",
                    "Axon Active là công ty phần mềm Thụy Sĩ với văn phòng tại Việt Nam, chuyên phát triển phần mềm Agile.",
                    "Hồ Chí Minh", "Bến Thành", "Tầng 8, Tòa nhà Viet Dragon, 141 Nguyễn Huệ"},

            {"NashTech Vietnam", TYPE_OUTSOURCE, SIZE_LARGE, "0500223344",
                    "NashTech là công ty công nghệ thuộc tập đoàn Harvey Nash, chuyên cung cấp giải pháp phần mềm.",
                    "Hồ Chí Minh", "Tân Bình", "Tầng 9, Tòa nhà E-Town 2, 364 Cộng Hòa"},
    };

    private static final Object[][] EMPLOYERS = {
            {"employer_fpt", "employer@fpt-software.com", "Minh", "Nguyen", "MALE", "0101248141", STATUS_APPROVED},
            {"employer_vng", "employer@vng.com.vn", "Lan", "Tran", "FEMALE", "0303456789", STATUS_APPROVED},
            {"employer_tiki", "employer@tiki.vn", "Huy", "Le", "MALE", "0312345678", STATUS_APPROVED},
            {"employer_axon", "employer@axonactive.com", "Linh", "Pham", "FEMALE", "0400112233", STATUS_APPROVED},
            {"employer_nash", "employer@nashtech.com", "Duc", "Vo", "MALE", "0500223344", STATUS_APPROVED},
    };

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String PASSWORD = "employer123";


    @Override
    public void migrate(Context context) throws Exception {
        Connection conn = context.getConnection();

        Long countryId = findCountryId(conn, COUNTRY_VN);
        Long adminId = findAdminUserId(conn);

        for (Object[] c : COMPANIES) {
            String taxCode = (String) c[3];
            if (findCompanyByTaxCode(conn, taxCode) != null) continue;

            String cityKw = (String) c[5];
            String districtKw = (String) c[6];
            String streetAddr = (String) c[7];

            Long cityId = findCityByKeyword(conn, cityKw);
            if (cityId == null) continue;

            Long districtId = findDistrictByKeyword(conn, cityId, districtKw);
            if (districtId == null) continue;

            Long addressId = findOrCreateAddress(conn, cityId, districtId, streetAddr);

            insertCompany(conn,
                    (String) c[0], // name
                    (String) c[1], // type
                    (String) c[2], // employee_size
                    taxCode,
                    (String) c[4], // description
                    COMPANY_APPROVED,
                    countryId,
                    addressId      // address_id
            );
        }

        // 2. Seed employer users + profiles (giữ nguyên như cũ)
        Long employerGroupId = findOrCreateGroup(conn, "EMPLOYER");

        for (Object[] e : EMPLOYERS) {
            String username = (String) e[0];
            String email = (String) e[1];
            String firstName = (String) e[2];
            String lastName = (String) e[3];
            String gender = (String) e[4];
            String taxCode = (String) e[5];
            String status = (String) e[6];

            Long userId = findUserByUsername(conn, username);
            if (userId == null) {
                userId = insertUser(conn, username, email, String.format("%s %s", firstName, lastName),
                        gender, encoder.encode(PASSWORD), "EMPLOYER");
                addUserToGroup(conn, userId, employerGroupId);
            }

            Long companyId = findCompanyByTaxCode(conn, taxCode);
            if (companyId == null) continue;

            boolean isApproved = STATUS_APPROVED.equals(status);
            insertEmployerProfileIfAbsent(conn, userId, companyId, status,
                    isApproved ? adminId : null,
                    isApproved ? OffsetDateTime.now() : null
            );
        }
    }


    private Long findCountryId(Connection conn, String code) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM countries WHERE code = ?")) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findAdminUserId(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM users WHERE user_role = 'ADMIN' LIMIT 1")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findCompanyByTaxCode(Connection conn, String taxCode) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM companies WHERE tax_code = ?")) {
            ps.setString(1, taxCode);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findCityByKeyword(Connection conn, String keyword) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM cities WHERE name ILIKE ? LIMIT 1")) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findDistrictByKeyword(Connection conn, Long cityId, String keyword)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM districts WHERE city_id = ? AND name ILIKE ? LIMIT 1")) {
            ps.setLong(1, cityId);
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findUserByUsername(Connection conn, String username) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findOrCreateGroup(Connection conn, String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM roles WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO roles (name) VALUES (?) RETURNING id")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    private Long insertCompany(Connection conn, String name, String type,
                               String employeeSize, String taxCode,
                               String description, String status,
                               Long countryId, Long addressId) throws SQLException {
        String slug = generateSlug(name);
        String sql = """
                INSERT INTO companies
                    (name, slug, type, employee_size, tax_code, description, status, country_id, address_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, slug);
            ps.setString(3, type);
            ps.setString(4, employeeSize);
            ps.setString(5, taxCode);
            ps.setString(6, description);
            ps.setString(7, status);
            if (countryId != null) ps.setLong(8, countryId);
            else ps.setNull(8, Types.BIGINT);
            ps.setLong(9, addressId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    private String generateSlug(String name) {
        String normalized = java.text.Normalizer
                .normalize(name, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");


        return normalized
                .toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private Long findOrCreateAddress(Connection conn, Long cityId,
                                     Long districtId, String streetAddress)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM addresses WHERE city_id = ? AND district_id = ? AND street_address = ?")) {
            ps.setLong(1, cityId);
            ps.setLong(2, districtId);
            ps.setString(3, streetAddress);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO addresses (city_id, district_id, street_address) VALUES (?, ?, ?) RETURNING id")) {
            ps.setLong(1, cityId);
            ps.setLong(2, districtId);
            ps.setString(3, streetAddress);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }


    private Long insertUser(Connection conn, String username, String email,
                            String name, String gender, String hashedPassword,
                            String role) throws SQLException {
        String sql = """
                INSERT INTO users
                    (username, email, name, gender, password_hash, user_role)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, name);
            ps.setString(4, gender);
            ps.setString(5, hashedPassword);
            ps.setString(6, role);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    private void addUserToGroup(Connection conn, Long userId, Long groupId)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?) ON CONFLICT DO NOTHING")) {
            ps.setLong(1, userId);
            ps.setLong(2, groupId);
            ps.executeUpdate();
        }
    }

    private void insertEmployerProfileIfAbsent(Connection conn, Long userId,
                                               Long companyId, String status,
                                               Long approvedBy,
                                               OffsetDateTime approvedAt)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT user_id FROM employer_profiles WHERE user_id = ?")) {
            ps.setLong(1, userId);
            if (ps.executeQuery().next()) return;
        }
        String sql = """
                INSERT INTO employer_profiles
                    (user_id, company_id, status, approved_by_id, approved_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, companyId);
            ps.setString(3, status);
            if (approvedBy != null) ps.setLong(4, approvedBy);
            else ps.setNull(4, Types.BIGINT);
            if (approvedAt != null) ps.setObject(5, approvedAt);
            else ps.setNull(5, Types.TIMESTAMP_WITH_TIMEZONE);
            ps.executeUpdate();
        }
    }
}