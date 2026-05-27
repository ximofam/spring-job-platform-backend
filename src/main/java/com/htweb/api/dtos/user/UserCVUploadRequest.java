package com.htweb.api.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserCVUploadRequest {
    @NotBlank(message = "Vui lòng nhập tiêu đề cho cv của bạn")
    private String title;
    @NotNull(message = "Vui lòng chọn một file ảnh để tải lên!")
    private MultipartFile file;
}
