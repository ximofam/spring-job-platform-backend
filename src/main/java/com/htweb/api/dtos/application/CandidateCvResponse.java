package com.htweb.api.dtos.application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateCvResponse {
    private Long id;
    private String title;
    private String fileUrl;
}
