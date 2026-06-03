package com.htweb.api.dtos.job;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class JobComparationResponse {
    private Long   jobId;
    private String title;
    private double score;   // 0.0 – 100.0
}