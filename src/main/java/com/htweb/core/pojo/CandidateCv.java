package com.htweb.core.pojo;

import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidate_cvs")
@Getter
@Setter
@NoArgsConstructor
public class CandidateCv extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id", nullable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;
}
