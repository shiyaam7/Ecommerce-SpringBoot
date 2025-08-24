package com.springboot.store.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {

    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
