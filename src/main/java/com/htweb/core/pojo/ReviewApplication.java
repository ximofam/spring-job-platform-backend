package com.htweb.core.pojo;

import com.htweb.core.enums.ApplicationStatus;
import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_applications")
@Getter
@Setter
@NoArgsConstructor
public class ReviewApplication extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    @Column(name = "application_id", insertable = false, updatable = false)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private ApplicationStatus oldStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private ApplicationStatus newStatus;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
}
