package com.htweb.core.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile implements Serializable {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "candidateProfile", fetch = FetchType.LAZY)
    private Set<Education> educations;

    @OneToMany(mappedBy = "candidateProfile", fetch = FetchType.LAZY)
    private Set<Experience> experiences;

    @OneToMany(mappedBy = "candidateProfile", fetch = FetchType.LAZY)
    private Set<CandidateCv> candidateCvs;

    @OneToMany(mappedBy = "candidateProfile", fetch = FetchType.LAZY)
    private Set<Application> applications;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateProfile other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
