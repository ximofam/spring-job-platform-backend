package db.seed.postgresql;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class V14__seed_jobs extends BaseJavaMigration {

    private static final Object[][] JOBS = {
            {"0101248141", "Senior Java Backend Developer", "Software Development",
                    "FULL_TIME", "SENIOR", "PUBLISHED",
                    40_000_000L, 70_000_000L, "VND", 30,
                    "FPT Software tìm kiếm Senior Java Developer có kinh nghiệm xây dựng hệ thống backend hiệu suất cao cho các dự án outsourcing quốc tế.\n\nBạn sẽ tham gia vào đội ngũ phát triển phần mềm cho khách hàng tại Nhật Bản, Hàn Quốc và châu Âu, làm việc với các công nghệ hiện đại trong môi trường Agile/Scrum chuyên nghiệp.",
                    "- Tối thiểu 4 năm kinh nghiệm với Java (Spring Boot, Spring MVC, Spring Data)\n- Thành thạo thiết kế RESTful API và microservices architecture\n- Kinh nghiệm với PostgreSQL / MySQL và tối ưu hóa query\n- Hiểu biết về Docker, Kubernetes là lợi thế\n- Khả năng đọc hiểu tài liệu và giao tiếp bằng tiếng Anh (B2 trở lên)\n- Có kinh nghiệm làm việc với mô hình Agile/Scrum",
                    "- Lương cạnh tranh, review 2 lần/năm\n- Thưởng dự án, thưởng hiệu suất cuối năm\n- 15 ngày phép/năm + các ngày lễ theo quy định\n- Bảo hiểm sức khỏe cao cấp cho nhân viên và người thân\n- Cơ hội công tác nước ngoài (Nhật, Hàn, EU)\n- Lộ trình thăng tiến rõ ràng: Senior → Tech Lead → Architect"},

            {"0101248141", "DevOps / Cloud Engineer", "Cloud Computing",
                    "FULL_TIME", "MIDDLE", "PUBLISHED",
                    30_000_000L, 55_000_000L, "VND", 45,
                    "Chúng tôi đang tìm kiếm DevOps Engineer để xây dựng và vận hành hạ tầng cloud cho các hệ thống phần mềm quy mô lớn phục vụ khách hàng toàn cầu của FPT Software.",
                    "- 2–4 năm kinh nghiệm DevOps/Cloud\n- Thành thạo ít nhất một nền tảng cloud: AWS, GCP hoặc Azure\n- Kinh nghiệm CI/CD: Jenkins, GitLab CI, GitHub Actions\n- Sử dụng thành thạo Docker và Kubernetes\n- Kinh nghiệm với Infrastructure as Code (Terraform, Ansible)\n- Hiểu biết về monitoring: Prometheus, Grafana, ELK Stack",
                    "- Lương thỏa thuận theo năng lực\n- Hỗ trợ thi chứng chỉ cloud (AWS/GCP/Azure) 100%\n- Làm việc với hệ thống quy mô lớn, môi trường quốc tế\n- Chế độ WFH linh hoạt 2 ngày/tuần\n- 14 ngày phép năm + nghỉ lễ đầy đủ"},

            {"0101248141", "Thực tập sinh Kiểm thử Phần mềm (QA Intern)", "Software Testing (QA/QC)",
                    "INTERNSHIP", "INTERN", "PUBLISHED",
                    3_000_000L, 5_000_000L, "VND", 20,
                    "FPT Software mở chương trình thực tập dành cho sinh viên ngành CNTT muốn tìm hiểu về quy trình kiểm thử phần mềm trong môi trường doanh nghiệp lớn.",
                    "- Sinh viên năm 3, năm 4 ngành CNTT hoặc liên quan\n- Có kiến thức cơ bản về SDLC và vòng đời kiểm thử\n- Ham học hỏi, chịu khó, có tinh thần trách nhiệm\n- Ưu tiên ứng viên đã biết Selenium, Postman hoặc JMeter",
                    "- Trợ cấp thực tập hấp dẫn\n- Được mentor 1-1 bởi QA senior có kinh nghiệm\n- Cấp laptop trong thời gian thực tập\n- Cơ hội được nhận vào làm chính thức sau thực tập\n- Cấp chứng nhận thực tập có giá trị"},


            {"0303456789", "Senior Backend Engineer (Golang)", "Software Development",
                    "FULL_TIME", "SENIOR", "PUBLISHED",
                    50_000_000L, 90_000_000L, "VND", 30,
                    "VNG tìm kiếm Backend Engineer (Golang) để phát triển các dịch vụ core cho nền tảng Zalo — ứng dụng nhắn tin với hơn 75 triệu người dùng tại Việt Nam.\n\nBạn sẽ làm việc trong team hệ thống phân tán, xử lý hàng tỷ message mỗi ngày, với yêu cầu cao về hiệu suất, độ ổn định và khả năng mở rộng.",
                    "- 4+ năm kinh nghiệm backend, tối thiểu 2 năm với Golang\n- Hiểu sâu về distributed systems, message queue (Kafka, RabbitMQ)\n- Kinh nghiệm xây dựng và vận hành microservices ở quy mô lớn\n- Thành thạo Redis, MySQL/PostgreSQL\n- Hiểu biết về low-latency system design\n- Có kinh nghiệm với gRPC là điểm cộng",
                    "- Mức lương top thị trường, review hàng năm\n- Cổ phần ESOP cho nhân sự cấp cao\n- Campus VNG đẳng cấp: gym, bể bơi, sân thể thao\n- Bữa trưa miễn phí tại canteen\n- Bảo hiểm sức khỏe VIP cho cả gia đình\n- Budget học tập và tham dự hội nghị quốc tế"},

            {"0303456789", "AI/ML Engineer – Recommendation System", "AI & Machine Learning",
                    "FULL_TIME", "MIDDLE", "PUBLISHED",
                    40_000_000L, 75_000_000L, "VND", 40,
                    "Nhóm AI của VNG đang phát triển hệ thống recommendation và ranking cho các sản phẩm nội dung (ZingMP3, ZingTV). Bạn sẽ nghiên cứu và triển khai các thuật toán gợi ý cá nhân hóa phục vụ hàng chục triệu người dùng.",
                    "- 2–5 năm kinh nghiệm Machine Learning / Deep Learning\n- Thành thạo Python, PyTorch hoặc TensorFlow\n- Kinh nghiệm với collaborative filtering, content-based filtering, hoặc two-tower model\n- Hiểu biết về xây dựng feature pipeline và MLOps\n- Có kinh nghiệm triển khai model lên production\n- Nền tảng toán học vững: xác suất, đại số tuyến tính, tối ưu hóa",
                    "- Làm việc với data thực tế quy mô hàng chục triệu user\n- Môi trường nghiên cứu cởi mở, khuyến khích publish paper\n- Budget GPU/cloud không giới hạn cho experiment\n- Tham gia các hội nghị AI quốc tế (NeurIPS, ICML)\n- Lương cạnh tranh + thưởng hiệu suất"},

            {"0312345678", "Frontend Engineer (React/TypeScript)", "Software Development",
                    "FULL_TIME", "JUNIOR", "PUBLISHED",
                    18_000_000L, 30_000_000L, "VND", 25,
                    "Tiki đang tìm kiếm Frontend Engineer gia nhập đội ngũ phát triển giao diện cho nền tảng thương mại điện tử hàng đầu Việt Nam với hàng triệu lượt truy cập mỗi ngày.",
                    "- 1–3 năm kinh nghiệm Frontend\n- Thành thạo React.js và TypeScript\n- Có kiến thức tốt về HTML5, CSS3, responsive design\n- Hiểu biết về state management (Redux, Zustand hoặc React Query)\n- Quen thuộc với Git workflow và code review\n- Có kinh nghiệm tối ưu hóa Web Performance là lợi thế",
                    "- Lương hấp dẫn + thưởng theo quý\n- Làm việc với traffic thực tế hàng triệu user\n- Văn phòng trung tâm, cơ sở vật chất hiện đại\n- Bảo hiểm sức khỏe, nghỉ phép 14 ngày/năm\n- Chương trình đào tạo và phát triển kỹ năng"},

            {"0312345678", "Data Analyst – E-commerce Insights", "Data & Analytics",
                    "FULL_TIME", "MIDDLE", "PUBLISHED",
                    22_000_000L, 38_000_000L, "VND", 35,
                    "Tiki cần Data Analyst phân tích hành vi mua sắm, đo lường hiệu quả chiến dịch và cung cấp insight cho các đội product, marketing và kinh doanh.",
                    "- 2–4 năm kinh nghiệm Data Analytics\n- Thành thạo SQL (BigQuery, PostgreSQL)\n- Sử dụng tốt Python (pandas, numpy) hoặc R cho phân tích\n- Kinh nghiệm trực quan hóa dữ liệu: Tableau, Metabase hoặc Looker\n- Tư duy phân tích sắc bén, khả năng kể chuyện bằng dữ liệu\n- Kinh nghiệm trong lĩnh vực e-commerce hoặc fintech là lợi thế",
                    "- Tiếp cận dữ liệu thực tế quy mô lớn của sàn TMĐT top đầu\n- Môi trường data-driven, quyết định dựa trên số liệu\n- Laptop MacBook, màn hình ngoài\n- Team trẻ, năng động, định kỳ team building\n- Lương review hàng năm theo năng lực"},

            {"0400112233", "Full Stack Developer (.NET + Angular)", "Software Development",
                    "FULL_TIME", "MIDDLE", "PUBLISHED",
                    28_000_000L, 50_000_000L, "VND", 30,
                    "Axon Active Vietnam tìm kiếm Full Stack Developer để phát triển phần mềm doanh nghiệp cho khách hàng Thụy Sĩ trong các lĩnh vực tài chính, bảo hiểm và logistics theo quy trình Agile chuyên nghiệp.",
                    "- 2–5 năm kinh nghiệm phát triển phần mềm\n- Thành thạo .NET (ASP.NET Core, C#) ở phía backend\n- Có kinh nghiệm với Angular (v12+) ở phía frontend\n- Hiểu về cơ sở dữ liệu quan hệ (SQL Server, PostgreSQL)\n- Kinh nghiệm làm việc trong môi trường Agile/Scrum\n- Tiếng Anh đủ để đọc tài liệu và họp với khách hàng nước ngoài",
                    "- Làm việc trực tiếp với khách hàng Thụy Sĩ, môi trường quốc tế\n- Quy trình Agile chuẩn Swiss, team nhỏ gọn, tự chủ cao\n- 15 ngày phép/năm + du lịch công ty hàng năm\n- Bảo hiểm sức khỏe, hỗ trợ học phí nâng cao kỹ năng\n- WFH linh hoạt, giờ làm việc mềm dẻo"},

            {"0400112233", "Scrum Master / Agile Coach", "IT Project Management",
                    "FULL_TIME", "SENIOR", "PUBLISHED",
                    35_000_000L, 60_000_000L, "VND", 45,
                    "Axon Active tìm Scrum Master có kinh nghiệm để hỗ trợ nhiều đội Agile, thúc đẩy văn hóa cải tiến liên tục và đảm bảo chất lượng quy trình phát triển phần mềm theo chuẩn Swiss.",
                    "- Tối thiểu 3 năm kinh nghiệm ở vai trò Scrum Master\n- Có chứng chỉ CSM, PSM I trở lên\n- Hiểu sâu về Scrum, Kanban, SAFe\n- Kỹ năng facilitation, coaching và giải quyết xung đột tốt\n- Tiếng Anh thành thạo (giao tiếp hàng ngày với khách hàng nước ngoài)\n- Ưu tiên ứng viên có nền tảng kỹ thuật (từng là developer)",
                    "- Đồng hành cùng đội ngũ Agile từ Thụy Sĩ, học hỏi best practices quốc tế\n- Hỗ trợ thi các chứng chỉ Agile cấp cao (A-CSM, PSM II, SAFe)\n- Lương hấp dẫn, review định kỳ\n- Môi trường làm việc tin tưởng và trao quyền tối đa"},

            {"0500223344", "Security Engineer (Penetration Tester)", "Information Security",
                    "FULL_TIME", "MIDDLE", "PUBLISHED",
                    30_000_000L, 55_000_000L, "VND", 30,
                    "NashTech cần Security Engineer thực hiện đánh giá bảo mật, kiểm tra xâm nhập và tư vấn giải pháp bảo mật cho các khách hàng doanh nghiệp trong và ngoài nước.",
                    "- 2–5 năm kinh nghiệm về bảo mật thông tin\n- Thành thạo penetration testing (web, mobile, network)\n- Hiểu biết về OWASP Top 10, CVE, các kỹ thuật exploit phổ biến\n- Sử dụng thành thạo Burp Suite, Metasploit, Nmap, Nessus\n- Có chứng chỉ CEH, OSCP, hoặc tương đương là lợi thế lớn\n- Kỹ năng viết báo cáo pentest rõ ràng, chuyên nghiệp",
                    "- Tiếp cận đa dạng hệ thống từ nhiều ngành: ngân hàng, TMĐT, chính phủ\n- Hỗ trợ học và thi chứng chỉ bảo mật quốc tế (OSCP, CISSP)\n- Môi trường làm việc chuyên nghiệp theo tiêu chuẩn Harvey Nash Group\n- Chính sách remote linh hoạt\n- Bảo hiểm sức khỏe, thưởng hiệu suất hàng quý"},

            {"0500223344", "UI/UX Designer", "UI/UX Design",
                    "FULL_TIME", "JUNIOR", "DRAFT",
                    15_000_000L, 25_000_000L, "VND", 60,
                    "NashTech tìm kiếm UI/UX Designer để thiết kế giao diện và trải nghiệm người dùng cho các sản phẩm phần mềm B2B phục vụ khách hàng UK và châu Âu.",
                    "- 1–3 năm kinh nghiệm UI/UX Design\n- Thành thạo Figma (thiết kế, prototyping, design system)\n- Hiểu biết về user research, usability testing\n- Có portfolio thể hiện rõ quy trình design thinking\n- Tiếng Anh đọc hiểu tốt để làm việc với tài liệu và khách hàng quốc tế\n- Kiến thức cơ bản về HTML/CSS là điểm cộng",
                    "- Làm việc trong môi trường quốc tế, sản phẩm B2B đa lĩnh vực\n- Được đào tạo và hướng dẫn bởi senior designer người nước ngoài\n- Ngân sách mua tool design (Figma, Adobe CC...)\n- Giờ làm linh hoạt, văn phòng trung tâm thành phố"},
    };


    @Override
    public void migrate(Context context) throws Exception {
        Connection conn = context.getConnection();

        for (Object[] j : JOBS) {
            String taxCode = (String) j[0];
            String title = (String) j[1];
            String categoryName = (String) j[2];
            String employmentType = (String) j[3];
            String experienceLevel = (String) j[4];
            String status = (String) j[5];
            long salaryMin = (long) j[6];
            long salaryMax = (long) j[7];
            String salaryCurrency = (String) j[8];
            int expiredAtDays = (int) j[9];
            String description = (String) j[10];
            String requirements = (String) j[11];
            String benefit = (String) j[12];

            long[] companyInfo = findCompanyInfo(conn, taxCode);
            if (companyInfo == null) continue;
            long companyId = companyInfo[0];
            long addressId = companyInfo[1];

            Long categoryId = findCategoryByName(conn, categoryName);
            if (categoryId == null) continue;

            if (jobExists(conn, companyId, title)) continue;

            insertJob(conn,
                    title, categoryId, employmentType, experienceLevel,
                    status, addressId, salaryMin, salaryMax, salaryCurrency,
                    OffsetDateTime.now().plusDays(expiredAtDays),
                    description, requirements, benefit,
                    companyId
            );
        }
    }

    private long[] findCompanyInfo(Connection conn, String taxCode) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, address_id FROM companies WHERE tax_code = ?")) {
            ps.setString(1, taxCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new long[]{rs.getLong(1), rs.getLong(2)};
            return null;
        }
    }

    private Long findCategoryByName(Connection conn, String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM categories WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private Long findEmployerUserIdByCompany(Connection conn, long companyId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT user_id FROM employer_profiles WHERE company_id = ? LIMIT 1")) {
            ps.setLong(1, companyId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : null;
        }
    }

    private boolean jobExists(Connection conn, long companyId, String title)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM jobs WHERE company_id = ? AND title = ?")) {
            ps.setLong(1, companyId);
            ps.setString(2, title);
            return ps.executeQuery().next();
        }
    }

    private void insertJob(Connection conn,
                           String title, Long categoryId,
                           String employmentType, String experienceLevel,
                           String status, long addressId,
                           long salaryMin, long salaryMax, String salaryCurrency,
                           OffsetDateTime expiredAt,
                           String description, String requirements, String benefit,
                           long companyId) throws SQLException {
        String sql = """
                INSERT INTO jobs (
                    title, category_id, employment_type, experience_level,
                    status, address_id, salary_min, salary_max, salary_currency,
                    expired_at, description, requirements, benefit, company_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setLong(2, categoryId);
            ps.setString(3, employmentType);
            ps.setString(4, experienceLevel);
            ps.setString(5, status);
            ps.setLong(6, addressId);
            ps.setLong(7, salaryMin);
            ps.setLong(8, salaryMax);
            ps.setString(9, salaryCurrency);
            ps.setObject(10, expiredAt);
            ps.setString(11, description);
            ps.setString(12, requirements);
            ps.setString(13, benefit);
            ps.setLong(14, companyId);
            ps.executeUpdate();
        }
    }
}