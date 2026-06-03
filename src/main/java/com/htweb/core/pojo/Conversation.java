package com.htweb.core.pojo;

import com.htweb.core.enums.ConversationType;
import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
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
    private Instant lastMessageAt = Instant.now();

    @Column(name = "room_hash")
    private String roomHash;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    @BatchSize(size = 20)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<ConversationMember> members = new HashSet<>();

    public void addMember(ConversationMember member) {
        this.members.add(member);
        member.setConversation(this);
    }
}
