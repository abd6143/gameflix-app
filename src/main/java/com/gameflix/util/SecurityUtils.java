package com.gameflix.util;

import com.gameflix.entity.User;
import com.gameflix.exception.UnauthorizedException;
import com.gameflix.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserService userService;

    public SecurityUtils(UserService userService) {
        this.userService = userService;
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("Not authenticated");
        }
        return authentication.getName();
    }

    public User getCurrentUser() {
        return userService.findByEmail(getCurrentUserEmail());
    }
}
