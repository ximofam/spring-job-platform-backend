package com.htweb.core.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htweb.core.services.EmbedService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class EmbedServiceImpl implements EmbedService {
    private final String apiUrl;
    private final String apiKey;
    private final int timeoutMs;
    private final int requestTimeoutMs;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public EmbedServiceImpl(
            @Value("${app.ai.embed.url:http://localhost:8000/embed}") String apiUrl,
            @Value("${app.ai.embed.api-key}") String apiKey,
            @Value("${app.ai.embed.timeout-ms:3000}") int timeoutMs,
            @Value("${app.ai.embed.request-timeout-ms:7000}") int requestTimeoutMs) {

        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.timeoutMs = timeoutMs;
        this.requestTimeoutMs = requestTimeoutMs;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeoutMs))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public float[] getEmbedding(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        float[][] result = getEmbeddings(List.of(text));
        return (result != null && result.length > 0) ? result[0] : null;
    }


    public float[][] getEmbeddings(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return new float[0][0];
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("texts", texts);
            String requestBody = objectMapper.writeValueAsString(payload);
            System.out.println(apiUrl);
            System.out.println(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofMillis(requestTimeoutMs))
                    .header("Content-Type", "application/json")
                    .header("X-API-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API Embedding trả về lỗi: " + response.statusCode() + " - " + response.body());
            }

            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode embeddingsNode = rootNode.get("embeddings");

            if (embeddingsNode == null || !embeddingsNode.isArray()) {
                throw new RuntimeException("Phản hồi từ AI Server không đúng định dạng.");
            }

            int rowCount = embeddingsNode.size();
            int colCount = embeddingsNode.get(0).size();
            float[][] embeddings = new float[rowCount][colCount];

            for (int i = 0; i < rowCount; i++) {
                JsonNode vectorNode = embeddingsNode.get(i);
                for (int j = 0; j < colCount; j++) {
                    embeddings[i][j] = vectorNode.get(j).floatValue();
                }
            }

            return embeddings;

        } catch (Exception e) {
            System.err.println("[EmbedService] Lỗi khi gọi AI Server: " + e.getMessage());
            return null;
        }
    }
}
