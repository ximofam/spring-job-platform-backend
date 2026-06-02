package db.seed.postgresql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class V21__update_jobs_embedding extends BaseJavaMigration {

    private static final int BATCH_SIZE = 50;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    @Override
    public void migrate(Context context) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String host = dotenv.get("EMBED_HOST");
        if (host == null || host.isBlank() || host.equals("0.0.0.0")) {
            host = "localhost";
        }
        String port = dotenv.get("EMBED_PORT");
        String apiUrl = "http://" + host + ":" + port + "/embed";
        String apiKey = dotenv.get("EMBED_API_KEY");

        Connection connection = context.getConnection();

        String selectSql = """
                SELECT id,
                       'Tiêu đề công việc: ' || COALESCE(title, '') || E'\\n' ||
                       'Mô tả công việc: ' || COALESCE(description, '') || E'\\n' ||
                       'Yêu cầu ứng viên: ' || COALESCE(requirements, '') || E'\\n' ||
                       'Phúc lợi và đãi ngộ: ' || COALESCE(benefit, '') AS full_text
                FROM jobs
                WHERE deleted_at IS NULL
                  AND embedding IS NULL
                """;

        List<JobData> pendingJobs = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             ResultSet rs = selectStmt.executeQuery()) {
            while (rs.next()) {
                pendingJobs.add(new JobData(rs.getLong("id"), rs.getString("full_text")));
            }
        }

        if (pendingJobs.isEmpty()) {
            System.out.println("V14: Không có Job nào cần tạo vector.");
            return;
        }

        System.out.println("V14: Bắt đầu tạo vector cho " + pendingJobs.size() + " jobs qua API " + apiUrl);

        String updateSql = "UPDATE jobs SET embedding = ?::vector WHERE id = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            for (int i = 0; i < pendingJobs.size(); i += BATCH_SIZE) {
                int end = Math.min(pendingJobs.size(), i + BATCH_SIZE);
                List<JobData> batch = pendingJobs.subList(i, end);

                List<String> texts = batch.stream().map(JobData::getText).collect(Collectors.toList());
                Map<String, Object> payload = new HashMap<>();
                payload.put("texts", texts);
                String requestBody = objectMapper.writeValueAsString(payload);
                System.out.println(apiUrl);
                System.out.println(requestBody);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .header("X-API-Key", apiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
                System.out.println("Body length = " + requestBody.length());

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("Lỗi gọi API Embedding: " + response.body());
                }

                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode embeddingsNode = rootNode.get("embeddings");

                if (embeddingsNode == null || !embeddingsNode.isArray() || embeddingsNode.size() != batch.size()) {
                    throw new RuntimeException("Dữ liệu trả về từ API không khớp với số lượng job đã gửi.");
                }


                for (int j = 0; j < batch.size(); j++) {
                    long jobId = batch.get(j).getId();
                    JsonNode vectorNode = embeddingsNode.get(j);

                    String vectorString = vectorNode.toString();

                    updateStmt.setString(1, vectorString);
                    updateStmt.setLong(2, jobId);
                    updateStmt.addBatch();
                }

                updateStmt.executeBatch();
                System.out.println("V14: Đã xử lý " + end + "/" + pendingJobs.size() + " jobs.");
            }
        }
    }


    private static class JobData {
        private final long id;
        private final String text;

        public JobData(long id, String text) {
            this.id = id;
            this.text = text;
        }

        public long getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }
}