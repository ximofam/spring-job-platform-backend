package com.htweb.api.controllers;

import com.htweb.api.dtos.chat.ConversationSimpleResponse;
import com.htweb.api.dtos.chat.GetOrCreateConversationRequest;
import com.htweb.api.dtos.chat.SendMessageRequest;
import com.htweb.api.services.ChatService;
import com.htweb.core.helpers.dtos.MessageResponse;
import com.htweb.core.helpers.paginates.PaginateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiChatController {
    @Qualifier("apiChatService")
    private final ChatService chatService;

    @GetMapping("/conversations")
    public ResponseEntity<PaginateResponse<ConversationSimpleResponse>> getConversations(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        return ResponseEntity.ok(chatService.getConversations(userId, page, size));
    }

    @PostMapping("/conversations/private")
    public ResponseEntity<ConversationSimpleResponse> getOrCreatePrivateConversation(
            @AuthenticationPrincipal Long userId,
            @RequestBody GetOrCreateConversationRequest request) {

        ConversationSimpleResponse response = chatService.getOrCreatePrivateConversation(userId, request.getPartnerId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<PaginateResponse<MessageResponse>> getMessages(
            @AuthenticationPrincipal Long userId,
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        return ResponseEntity.ok(chatService.getMessages(userId, id, page, size));
    }


    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid SendMessageRequest request) {

        return ResponseEntity.status(201).body(chatService.sendMessage(userId, request));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<MessageResponse> deleteMessage(
            @AuthenticationPrincipal Long userId,
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(chatService.deleteMessage(userId, id));
    }
}
