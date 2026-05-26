package com.htweb.api.dtos.locations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class DistrictSimpleResponse implements Serializable {
    private Long id;
    private String name;
}
