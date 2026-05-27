package com.htweb.core.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.htweb.core.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class StorageServiceImpl implements StorageService {

    private final Cloudinary cloudinary;
    private final S3Client s3Client;

    @Value("${supabase.s3.bucket}")
    private String supabaseBucket;

    @Value("${supabase.s3.project-id}")
    private String supabaseProjectId;


    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        validateFile(file);

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("File tải lên không phải là định dạng hình ảnh!");
        }

        Map<?, ?> options = ObjectUtils.asMap(
                "folder", "job_platform/images",
                "unique_filename", true
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");
    }

    @Override
    public String uploadPdf(MultipartFile file, String targetFolder) throws IOException {
        validateFile(file);

        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("File tải lên không phải là định dạng PDF!");
        }

        if (!targetFolder.endsWith("/")) {
            targetFolder += "/";
        }
        String fileName = targetFolder + UUID.randomUUID() + ".pdf";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(supabaseBucket)
                .key(fileName)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("https://%s.supabase.co/storage/v1/object/public/%s/%s",
                supabaseProjectId, supabaseBucket, fileName);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống!");
        }
        if (file.getContentType() == null) {
            throw new IllegalArgumentException("Không thể nhận diện được định dạng file!");
        }
    }
}