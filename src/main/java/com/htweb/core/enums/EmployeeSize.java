package com.htweb.core.enums;

import lombok.Getter;

@Getter
public enum EmployeeSize {
    SMALL("1 - 50"),
    MEDIUM("51 - 200"),
    LARGE("201 - 500"),
    VERY_LARGE("501 - 1000"),
    ENTERPRISE("1000+");

    private final String label;

    EmployeeSize(String label) {
        this.label = label;
    }

}
