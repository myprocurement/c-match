package com.avricot.cboost.security;

import com.avricot.cboost.domain.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Returns the current {@link User} or throws an
     * {@link CurrentUserNotFoundException} if the user is not authenticated or
     * if there is a security issue.
     */
    public static User getCurrentUser() {
        final Authentication authentication = SecurityUtils.getAuthentication();
        if (authentication == null) {
            throw new CurrentUserNotFoundException("Current user not found in security context");
        }
        if (!authentication.isAuthenticated()) {
            throw new CurrentUserNotFoundException("Current user is not authenticated");

        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new CurrentUserNotFoundException("Current user is anonymous");
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }

    /**
     * Private method that returns the {@link org.springframework.security.core.Authentication} contains in the
     * current {@link org.springframework.security.core.context.SecurityContext}
     * .
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
