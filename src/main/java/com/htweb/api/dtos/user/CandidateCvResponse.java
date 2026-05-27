package com.htweb.api.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateCvResponse {
    private Long id;
    private String title;
    private String fileUrl;
}
