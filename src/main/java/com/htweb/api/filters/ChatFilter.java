package com.htweb.api.filters;

import com.htweb.core.helpers.queries.CriteriaFilter;
import com.htweb.core.pojo.Conversation;
import com.htweb.core.pojo.Message;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;


public class ChatFilter {
    public static CriteriaFilter<Conversation> byUserId(Long userId) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> membersJoin = root.join("members", JoinType.INNER);

            if (userId != null) {
                predicates.add(cb.equal(membersJoin.get("user").get("id"), userId));
            }

            if (query.getResultType() != Long.class) {
                Fetch<Object, Object> membersFetch = root.fetch("members", JoinType.LEFT);
                membersFetch.fetch("user", JoinType.LEFT);
                root.fetch("messages", JoinType.LEFT);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static CriteriaFilter<Message> byConversationId(Long conversationId) {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class) {
                root.fetch("sender", JoinType.INNER);
            }

            return cb.equal(root.get("conversation").get("id"), conversationId);
        };
    }
}
