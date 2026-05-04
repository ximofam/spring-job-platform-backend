package com.htweb.core.pojo;

import com.htweb.core.enums.Degree;
import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Education extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id", nullable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "school", length = 200, nullable = false)
    private String school;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "major", length = 150, nullable = false)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree", length = 20, nullable = false)
    private Degree degree;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

}
