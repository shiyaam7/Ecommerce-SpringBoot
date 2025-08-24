package com.springboot.store.filters;

// Import JwtService which we will use to validate tokens
import com.springboot.store.services.JwtService;

// Import classes needed for working with HTTP requests and responses
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lombok annotation to automatically create a constructor with all arguments
import lombok.AllArgsConstructor;

// Spring Security classes for authentication handling
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Marks this class as a Spring-managed bean
import org.springframework.stereotype.Component;

// Special Spring Security filter that ensures execution once per request
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtAuthenticationFilter is a custom security filter
 * - It intercepts every incoming request
 * - Extracts and validates JWT tokens
 * - If valid, it sets authentication in the SecurityContext
 */
@AllArgsConstructor      // generates a constructor with JwtService as argument
@Component               // makes this class a Spring Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Dependency: service class that validates and extracts info from JWT
    private final JwtService jwtService;

    // Core method where filtering logic happens
    @Override
    protected void doFilterInternal(HttpServletRequest request,   // Incoming HTTP request
                                    HttpServletResponse response, // Outgoing HTTP response
                                    FilterChain filterChain)      // Chain of filters
            throws ServletException, IOException {

        // Step 1: Extract Authorization header from request
        var authHeader = request.getHeader("Authorization");

        // Step 2: If no header OR header does not start with "Bearer ", skip JWT processing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pass request to next filter
            return; // Stop current filter execution
        }

        // Step 3: Extract actual token (remove "Bearer " prefix)
        var token = authHeader.replace("Bearer ", "");

        // Step 4: Validate token using JwtService
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response); // Token invalid → skip
            return;
        }

        // Step 5: Create Authentication object (Spring Security uses this to know user is authenticated)
//        var authentication = new UsernamePasswordAuthenticationToken(
//                jwtService.getEmailFromToken(token), // Principal (here: email from token)
//                null,                                // No credentials (already validated by JWT)
//                null                                 // No authorities/roles for now
//        );

        var userId = jwtService.getUserIdFromToken(token);
        var role = jwtService.getRoleFromToken(token);

        var authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

        // Step 6: Attach request details (IP, session ID, etc.) to authentication object
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // Step 7: Store authentication object in SecurityContext (Spring Security’s "current user")
        /**
         * Once you put that in SecurityContextHolder,
         *  Spring Security considers the request authenticated.
         *
         * So when execution reaches UsernamePasswordAuthenticationFilter,
         *  it simply skips asking for credentials, since authentication is already set.
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Step 8: Continue with next filter in chain
        filterChain.doFilter(request, response);
    }
}
