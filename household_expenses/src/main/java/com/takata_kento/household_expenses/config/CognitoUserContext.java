package com.takata_kento.household_expenses.config;

import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CognitoUserContext {

    public CognitoUserContext() {
        throw new AssertionError();
    }

    public static UserId currentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("No authenticated JWT principal found in SecurityContext");
        }
        return new UserId(UUID.fromString(jwt.getSubject()));
    }
}
