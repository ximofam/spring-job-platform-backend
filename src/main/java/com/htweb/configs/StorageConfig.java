package com.htweb.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class StorageConfig {
    @Value("${supabase.s3.project-id}")
    private String supabaseProjectId;
    @Value("${supabase.s3.access-key}")
    private String supabaseAccessKey;
    @Value("${supabase.s3.secret-key}")
    private String supabaseSecretKey;
    @Value("${supabase.s3.region}")
    private String supabaseRegion;
    @Value("${cloud.name}")
    private String cloudName;
    @Value("${cloud.api-key}")
    private String cloudApiKey;
    @Value("${cloud.api-secret}")
    private String cloudApiSecret;

    @Bean
    public S3Client s3Client() {
        String endpointUrl = String.format("https://%s.supabase.co/storage/v1/s3", supabaseProjectId);

        return S3Client.builder()
                .httpClient(UrlConnectionHttpClient.builder().build())
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.of(supabaseRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(supabaseAccessKey, supabaseSecretKey)
                ))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudApiKey,
                "api_secret", cloudApiSecret,
                "secure", true));
    }
}
