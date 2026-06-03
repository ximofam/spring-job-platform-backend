package com.htweb.api.services;

import com.htweb.api.dtos.chat.ConversationSimpleResponse;
import com.htweb.api.dtos.chat.SendMessageRequest;
import com.htweb.core.helpers.dtos.MessageResponse;
import com.htweb.core.helpers.paginates.PaginateResponse;

public interface ChatService {
    PaginateResponse<ConversationSimpleResponse> getConversations(Long userId, int page, int size);

    ConversationSimpleResponse getOrCreatePrivateConversation(Long currentUserId, Long partnerId);

    PaginateResponse<MessageResponse> getMessages(Long userId, Long conversationId, int page, int size);

    MessageResponse sendMessage(Long userId, SendMessageRequest request);

    MessageResponse deleteMessage(Long userId, Long messageId);
}
