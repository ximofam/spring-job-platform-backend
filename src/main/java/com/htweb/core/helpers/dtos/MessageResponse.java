package com.htweb.core.helpers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htweb.api.dtos.user.UserSimpleResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MessageResponse {
    private Long id;
    private Long conversationId;
    private String content;
    private UserSimpleResponse sender;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;
}
