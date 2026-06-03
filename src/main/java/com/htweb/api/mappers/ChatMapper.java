package com.htweb.api.mappers;


import com.htweb.api.dtos.chat.ConversationSimpleResponse;
import com.htweb.core.enums.ConversationType;
import com.htweb.core.helpers.dtos.MessageResponse;
import com.htweb.core.pojo.Conversation;
import com.htweb.core.pojo.Message;
import com.htweb.core.pojo.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChatMapper {

    @Autowired
    protected UserMapper userMapper;

    public abstract MessageResponse toMessageResponse(Message message);

    public abstract List<MessageResponse> toMessageResponseList(List<Message> messages);

    public abstract ConversationSimpleResponse toConversationSimpleResponse(Conversation conversation, @Context Long currentUserId);

    public abstract List<ConversationSimpleResponse> toConversationSimpleResponseList(List<Conversation> conversations, @Context Long currentUserId);

    @AfterMapping
    protected void enrichConversationResponse(
            @MappingTarget ConversationSimpleResponse response,
            Conversation conversation,
            @Context Long currentUserId) {

        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            Message lastMessage = conversation.getMessages().getFirst();
            response.setLastMessageContent(lastMessage.getContent());
        }

        if (ConversationType.PRIVATE.equals(conversation.getType()) && conversation.getMembers() != null) {
            conversation.getMembers().stream()
                    .filter(member -> !member.getUser().getId().equals(currentUserId))
                    .findFirst()
                    .ifPresent(partnerMember -> {
                        User partner = partnerMember.getUser();
                        response.setPartner(userMapper.toUserSimpleResponse(partner));
                    });
        }
    }
}
