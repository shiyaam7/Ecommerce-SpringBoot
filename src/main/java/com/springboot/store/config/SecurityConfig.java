package com.springboot.store.config;

import com.springboot.store.entities.Role;
import com.springboot.store.filters.JwtAuthenticationFilter;
import com.springboot.store.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a Spring configuration class (contains beans)
@EnableWebSecurity // Enables Spring Security in the project
@AllArgsConstructor // Generates constructor with required dependencies (injected via constructor)
public class SecurityConfig {

    // Dependency: custom service that loads user details (used by Spring Security)
    private final UserDetailsService userDetailsService;

    // Dependency: our custom JWT filter to validate tokens
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Bean for password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Uses BCrypt hashing algorithm (recommended & secure)
    }

    // Bean for AuthenticationProvider (tells Spring how to fetch users & validate credentials)
    @Bean
    public AuthenticationProvider authenticationProvider(){
        var provider = new DaoAuthenticationProvider(); // Provider that uses UserDetailsService + PasswordEncoder
        provider.setPasswordEncoder(passwordEncoder()); // Set password hashing mechanism
        provider.setUserDetailsService(userDetailsService); // Set service to load user details
        return provider;
    }

    // Bean for AuthenticationManager (main entry point for authentication)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Get AuthenticationManager from Spring context
    }

    // Bean for configuring Spring Security filter chain (main security rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Make Spring Security stateless (don’t use session, use JWT instead)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Disable CSRF protection (not needed for stateless JWT-based APIs)
                .csrf(c -> c.disable())

                // Define authorization rules for different endpoints
                .authorizeHttpRequests(c -> c
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/carts/**").permitAll() // Anyone can access cart APIs
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/users").permitAll() // Registration allowed
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/products/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll() // Login allowed
                                .requestMatchers(HttpMethod.POST,"/auth/refresh").permitAll() // Login allowed

//                  .requestMatchers(HttpMethod.POST,"/auth/validate").permitAll() // (optional) validate endpoint
                                .requestMatchers(HttpMethod.POST, "/checkout/webhook").permitAll()
                                .anyRequest().authenticated() // All other requests require authentication
                )

                // Register our JWT filter before Spring’s UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c ->{
                    c.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value())));
                    });
        return http.build(); // Build the security configuration
    }
}
