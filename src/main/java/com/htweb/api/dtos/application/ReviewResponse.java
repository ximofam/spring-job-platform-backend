package com.htweb.api.dtos.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private String oldStatus;
    private String newStatus;
    private String reason;
}
