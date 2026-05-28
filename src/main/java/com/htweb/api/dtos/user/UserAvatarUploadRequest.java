package com.htweb.api.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserAvatarUploadRequest {
    @NotNull(message = "Vui lòng chọn một file ảnh để tải lên!")
    private MultipartFile file;
}
