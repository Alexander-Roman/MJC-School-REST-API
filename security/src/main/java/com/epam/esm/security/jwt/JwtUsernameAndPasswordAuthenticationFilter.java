package com.epam.esm.security.jwt;

import com.epam.esm.security.model.EmailAndPasswordAuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        EmailAndPasswordAuthenticationRequest authenticationRequest;
        try {
            InputStream inputStream = request.getInputStream();
            authenticationRequest = new ObjectMapper()
                    .readValue(inputStream, EmailAndPasswordAuthenticationRequest.class);
        } catch (IOException e) {
            throw new SecurityException(e);
        }

        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String principalName = authResult.getName();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Date issueDate = new Date();

        int tokenDaysExpirationPeriod = jwtConfig.getTokenDaysExpirationPeriod();
        Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(tokenDaysExpirationPeriod));

        SecretKey secretKey = jwtConfig.secretKey();
        String token = Jwts.builder()
                .setSubject(principalName)
                .claim("authorities", authorities)
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
        String authorizationHeaderKey = jwtConfig.authorizationHeader();
        String tokenPrefix = jwtConfig.getTokenPrefix();
        response.addHeader(authorizationHeaderKey, tokenPrefix + token);
    }

}
