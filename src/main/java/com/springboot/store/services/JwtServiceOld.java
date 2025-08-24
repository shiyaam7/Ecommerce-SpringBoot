package com.springboot.store.services;

import com.springboot.store.config.JwtConfig;
import com.springboot.store.entities.Role;
import com.springboot.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service // Marks this class as a Spring service component so it can be injected elsewhere
@AllArgsConstructor
public class JwtServiceOld {

//    @Value("${spring.jwt.secret}") // Reads the secret key from application.properties (spring.jwt.secret)
//    private String secret;

    // Using Configurable Properties instead of Value
    private final JwtConfig jwtConfig;

    // Method to generate JWT token for a given email
    public String generateAccessToken(String email) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();

        return Jwts.builder() // Start building a JWT
                .subject(email) // Set email as the subject (the identity of the user)
                .issuedAt(new Date()) // Set issue time of token (current timestamp)
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration)) // Set expiry time (current time + validity duration)
                .signWith(jwtConfig.secretKey()) // Sign token with HMAC SHA using secret key
                .compact(); // Build the final JWT as a string
    }

    // Method to generate JWT token for a given user
    // changing subject to userId
    // and adding email and name in claim(additional property in payload)
    // adding 2 different methods for access token and refresh token
    public String generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.secretKey())
                .compact();
    }

    // Helper method to extract claims (payload) from a JWT token
    // Claims - are the properties we have in token payload
    private Claims getClaims(String token) {
        return Jwts.parser() // Create JWT parser
                .verifyWith(jwtConfig.secretKey()) // Set signing key to validate signature
                .build() // Build the parser
                .parseSignedClaims(token) // Parse the token (only if signature is valid)
                .getPayload(); // Get payload (claims) from the token
    }

    // Validate token - check if it's well-formed, signed, and not expired
    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token); // Extract claims from token
            return claims.getExpiration().after(new Date()); // Return true if token expiration date is after current time
        }
        catch (JwtException ex) { // If token is invalid (tampered/expired/etc.)
            return false; // Token is invalid
        }
    }

//    // Extract email (subject) from token
//    public String getEmailFromToken(String token) {
//        return getClaims(token).getSubject(); // Return the subject (email) stored in token
//    }

    // Extract id (subject) from token
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject()); // Return the subject (userId) stored in token
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}
