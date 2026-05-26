package com.htweb.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
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

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Number) {
            return ((Number) principal).longValue();
        }

        try {
            return Long.parseLong(principal.toString());
        } catch (NumberFormatException e) {

            log.error("Không thể parse userId sang Long từ principal: {}", principal, e);
            return null;
        }
    }

    public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptyList();
        }

        return authentication.getAuthorities();
    }
}
