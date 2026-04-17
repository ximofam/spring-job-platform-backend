package com.htweb.api.utils;

import java.util.List;

public class Utils {
    public static List<String> castToStringList(Object obj) {
        if (!(obj instanceof List<?> list)) {
            return List.of();
        }

        return list.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }
}
