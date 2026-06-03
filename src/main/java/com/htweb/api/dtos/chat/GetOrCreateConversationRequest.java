package com.htweb.api.dtos.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrCreateConversationRequest {
    private Long partnerId;
}
