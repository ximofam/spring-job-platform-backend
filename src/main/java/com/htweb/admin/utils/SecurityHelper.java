package com.htweb.admin.utils;

import com.htweb.core.helpers.security.CustomUserDetails;
import com.htweb.core.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelper {

    public CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public User getCurrentUser() {
        return getCurrentUserDetails().getUser();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}