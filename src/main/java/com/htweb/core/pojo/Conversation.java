package com.htweb.core.pojo;

import com.htweb.core.enums.ConversationType;
import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
public class Conversation extends BaseModel {
//    private String name;
//    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ConversationType type;

    @Column(name = "last_message_at")
    private Instant lastMessageAt;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY)
    private Set<ConversationMember> members;
}
