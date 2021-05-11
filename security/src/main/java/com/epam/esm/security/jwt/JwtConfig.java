package com.epam.esm.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;

@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {


    private final String secretPhrase;
    private final String tokenPrefix;
    private final Integer tokenDaysExpirationPeriod;
    private final String authorizationHeader;

    @ConstructorBinding
    public JwtConfig(String secretPhrase,
                     String tokenPrefix,
                     Integer tokenDaysExpirationPeriod,
                     String authorizationHeader) {
        this.secretPhrase = secretPhrase;
        this.tokenPrefix = tokenPrefix;
        this.tokenDaysExpirationPeriod = tokenDaysExpirationPeriod;
        this.authorizationHeader = authorizationHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Integer getTokenDaysExpirationPeriod() {
        return tokenDaysExpirationPeriod;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretPhrase.getBytes());
    }

    public String authorizationHeader() {
        return authorizationHeader;
    }

}
