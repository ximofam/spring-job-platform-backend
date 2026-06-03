package com.htweb.api.dtos.chat;

import com.htweb.api.dtos.user.UserSimpleResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversationSimpleResponse {
    private Long id;
    private String type;
    private UserSimpleResponse partner;
    private String lastMessageContent;
}
