package com.epam.esm.security.provider;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        OidcKeycloakAccount account = token.getAccount();
        KeycloakSecurityContext keycloakSecurityContext = account.getKeycloakSecurityContext();
        AccessToken accessToken = keycloakSecurityContext.getToken();

        Boolean emailVerified = accessToken.getEmailVerified();
        if (!emailVerified) {
            throw new KeycloakAuthenticationException("Email is not verified yet!");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<String> roles = account.getRoles();
        for (String role : roles) {
            KeycloakRole keycloakRole = new KeycloakRole(role);
            grantedAuthorities.add(keycloakRole);
        }

        String email = accessToken.getEmail();
        Principal principal = new KeycloakPrincipal<>(email, keycloakSecurityContext);
        RefreshableKeycloakSecurityContext refreshableKeycloakSecurityContext = (RefreshableKeycloakSecurityContext) keycloakSecurityContext;
        KeycloakAccount keycloakAccount = new SimpleKeycloakAccount(principal, roles, refreshableKeycloakSecurityContext);

        return new KeycloakAuthenticationToken(keycloakAccount, token.isInteractive(), grantedAuthorities);
    }

}
