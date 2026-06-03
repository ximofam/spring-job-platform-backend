package com.htweb.api.dtos.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private Long conversationId;
    @NotBlank(message = "Nội dung không được bỏ trống")
    private String content;
}
