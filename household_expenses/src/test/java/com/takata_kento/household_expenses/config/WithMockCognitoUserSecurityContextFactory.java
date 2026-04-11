package com.takata_kento.household_expenses.config;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

class WithMockCognitoUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCognitoUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCognitoUser annotation) {
        Jwt jwt = Jwt.withTokenValue("mock-token")
            .header("alg", "RS256")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .subject(annotation.sub())
            .claim("cognito:groups", Arrays.asList(annotation.groups()))
            .build();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.groups())
            .map(group -> new SimpleGrantedAuthority("ROLE_" + group.toUpperCase()))
            .toList();

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
