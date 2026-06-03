package com.htweb.api.services.impl;

import com.htweb.api.dtos.chat.ConversationSimpleResponse;
import com.htweb.api.dtos.chat.SendMessageRequest;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.ForbiddenException;
import com.htweb.api.filters.ChatFilter;
import com.htweb.api.mappers.ChatMapper;
import com.htweb.api.repositories.ConversationRepository;
import com.htweb.api.repositories.MessageRepository;
import com.htweb.api.repositories.UserRepository;
import com.htweb.api.services.ChatService;
import com.htweb.core.enums.ConversationType;
import com.htweb.core.helpers.dtos.MessageResponse;
import com.htweb.core.helpers.paginates.PaginateRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Conversation;
import com.htweb.core.pojo.ConversationMember;
import com.htweb.core.pojo.Message;
import com.htweb.core.pojo.User;
import com.htweb.core.tasks.SendMessageToUsersTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service("apiChatService")
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Qualifier("apiConversationRepository")
    private final ConversationRepository conversationRepository;
    private final ChatMapper chatMapper;
    @Qualifier("apiUserRepository")
    private final UserRepository userRepository;
    @Qualifier("apiMessageRepository")
    private final MessageRepository messageRepository;
    private final SendMessageToUsersTask sendMessageToUsersTask;

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse<ConversationSimpleResponse> getConversations(Long userId, int page, int size) {

        PaginateRequest<Conversation> request = PaginateRequest.<Conversation>builder()
                .page(page)
                .size(size)
                .filter(ChatFilter.byUserId(userId))
                .orderBy("lastMessageAt")
                .orderDesc(true)
                .build();


        return conversationRepository.paginate(request).map(conversations ->
                chatMapper.toConversationSimpleResponseList(conversations, userId));
    }

    @Override
    @Transactional
    public ConversationSimpleResponse getOrCreatePrivateConversation(Long currentUserId, Long partnerId) {
        if (currentUserId.equals(partnerId)) {
            throw new BadRequestException("Không thể tạo nhóm chat với chính mình.");
        }

        Optional<Conversation> existingConv = conversationRepository.findPrivateConversation(currentUserId, partnerId);

        if (existingConv.isPresent()) {
            return chatMapper.toConversationSimpleResponse(existingConv.get(), currentUserId);
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        User partnerUser = userRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner không tồn tại"));

        Conversation newConv = new Conversation();
        newConv.setType(ConversationType.PRIVATE);

        ConversationMember member1 = new ConversationMember();
        member1.setUser(currentUser);
        member1.setLastReadAt(Instant.now());

        ConversationMember member2 = new ConversationMember();
        member2.setUser(partnerUser);
        member2.setLastReadAt(Instant.now());

        newConv.addMember(member1);
        newConv.addMember(member2);

        conversationRepository.save(newConv);

        return chatMapper.toConversationSimpleResponse(newConv, currentUserId);
    }

    @Override
    public PaginateResponse<MessageResponse> getMessages(Long userId, Long conversationId, int page, int size) {
        if (!conversationRepository.isMemberOfConversation(userId, conversationId)) {
            throw new ForbiddenException("Bạn không có quyền xem tin nhắn của phòng chat này");
        }

        PaginateRequest<Message> request = PaginateRequest.<Message>builder()
                .page(page)
                .size(size)
                .filter(ChatFilter.byConversationId(conversationId))
                .orderBy("id")
                .orderDesc(true)
                .build();


        return messageRepository.paginate(request)
                .map(chatMapper::toMessageResponseList);
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(Long userId, SendMessageRequest request) {
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phòng chat này"));

        if (!conversationRepository.isMemberOfConversation(userId, conversation.getId())) {
            throw new ForbiddenException("Bạn không có quyền gửi tin nhắn vào phòng này");
        }

        User sender = userRepository.getReference(userId);

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());

        messageRepository.save(message);

        conversation.setLastMessageAt(Instant.now());
        conversationRepository.update(conversation);

        MessageResponse res = chatMapper.toMessageResponse(message);

        List<Long> memberUserIds = conversation.getMembers().stream()
                .map(member -> member.getUser().getId())
                .toList();
        sendMessageToUsersTask.execute(memberUserIds, res);

        return res;
    }

    @Override
    @Transactional
    public MessageResponse deleteMessage(Long userId, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new BadRequestException("Tin nhắn đã bị xóa hoặc không tồn tại"));

        if (!message.getSender().getId().equals(userId)) {
            throw new ForbiddenException("Bạn không được phép xóa tin nhắn này");
        }

        messageRepository.delete(messageId);

        return chatMapper.toMessageResponse(message);
    }
}
