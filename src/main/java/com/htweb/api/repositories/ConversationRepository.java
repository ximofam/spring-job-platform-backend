package com.htweb.api.repositories;

import com.htweb.core.pojo.Conversation;
import com.htweb.core.repositories.PaginateRepository;

import java.util.Optional;

public interface ConversationRepository extends PaginateRepository<Conversation, Long> {
    Optional<Conversation> findPrivateConversation(Long userId1, Long userId2);

    boolean isMemberOfConversation(Long userId, Long conversationId);
}
