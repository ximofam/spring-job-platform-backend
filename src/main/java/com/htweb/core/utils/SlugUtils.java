package com.htweb.core.utils;

import com.github.slugify.Slugify;

public class SlugUtils {
    private static final Slugify slugify = Slugify.builder()
            .lowerCase(true)
            .build();

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) return "";
        return slugify.slugify(input);
    }
}
