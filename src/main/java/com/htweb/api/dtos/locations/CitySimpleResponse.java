package com.htweb.api.dtos.locations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CitySimpleResponse implements Serializable {
    private Long id;
    private String name;
}
