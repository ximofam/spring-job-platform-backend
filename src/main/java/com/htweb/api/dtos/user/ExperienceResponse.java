package com.htweb.api.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExperienceResponse {
    private String company;
    private String position;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
