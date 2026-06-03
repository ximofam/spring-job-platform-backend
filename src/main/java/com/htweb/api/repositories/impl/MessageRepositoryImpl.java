package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.MessageRepository;
import com.htweb.core.pojo.Message;
import com.htweb.core.repositories.impl.PaginateRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiMessageRepository")
public class MessageRepositoryImpl extends PaginateRepositoryImpl<Message, Long> implements MessageRepository {
    public MessageRepositoryImpl() {
        super(Message.class);
    }
}
