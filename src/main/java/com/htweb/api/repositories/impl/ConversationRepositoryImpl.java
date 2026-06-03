package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.ConversationRepository;
import com.htweb.core.enums.ConversationType;
import com.htweb.core.pojo.Conversation;
import com.htweb.core.repositories.impl.PaginateRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("apiConversationRepository")
public class ConversationRepositoryImpl
        extends PaginateRepositoryImpl<Conversation, Long>
        implements ConversationRepository {
    public ConversationRepositoryImpl() {
        super(Conversation.class);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Conversation> findPrivateConversation(Long userId1, Long userId2) {
        String hql = """
                SELECT c FROM Conversation c
                JOIN c.members m1
                JOIN c.members m2
                WHERE c.type = :chatType
                  AND m1.user.id = :userId1
                  AND m2.user.id = :userId2
                """;

        return getCurrentSession().createQuery(hql, Conversation.class)
                .setParameter("chatType", ConversationType.PRIVATE)
                .setParameter("userId1", userId1)
                .setParameter("userId2", userId2)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isMemberOfConversation(Long userId, Long conversationId) {
        String hql = """
                SELECT COUNT(cm) FROM ConversationMember cm
                WHERE cm.conversation.id = :conversationId
                  AND cm.user.id = :userId
                """;

        Long count = getCurrentSession().createQuery(hql, Long.class)
                .setParameter("conversationId", conversationId)
                .setParameter("userId", userId)
                .uniqueResult();

        return count != null && count > 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Conversation> findByRoomHash(String roomHash) {
        String hql = "FROM Conversation c WHERE c.roomHash = :roomHash";

        return getCurrentSession().createQuery(hql, Conversation.class)
                .setParameter("roomHash", roomHash)
                .uniqueResultOptional();
    }
}
