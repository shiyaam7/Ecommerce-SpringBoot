package com.springboot.store.controllers;

import com.springboot.store.config.JwtConfig;
import com.springboot.store.dtos.JwtResponse;
import com.springboot.store.dtos.UserDto;
import com.springboot.store.dtos.UserLoginRequest;
import com.springboot.store.mappers.UserMapper;
import com.springboot.store.repositories.UserRepository;
import com.springboot.store.services.AuthService;
import com.springboot.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private UserMapper userMapper;
    private JwtConfig jwtConfig;
    private AuthService authService;

//    @PostMapping("/auth/login")
//    public ResponseEntity<?> login(
//            @Valid @RequestBody UserLoginRequest request
//            ){
////        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
////
////        if(user == null){
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
////        }
////
////        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
////        }
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        return ResponseEntity.ok().build();
//    }

// No need to move these logic in a service class
// because these logics has been already implemented by Spring Security
// There is a interface known as AuthenticationManager
// AUthController <- AuthenticationManager <- AuthenticationProvider <- DaoAuthenticationProvider(userDetailsService, passwordEncoder)

// 403 forbidden -> You're authenticated but dont have the permission to access the resource
// 401 -> You're not authenticated or your credentials are invalid


//    @PostMapping("/auth/login")
//    public ResponseEntity<JwtResponse> login(
//            @Valid @RequestBody UserLoginRequest request
//    ){
//        authenticationManager.authenticate(   //  Use the AuthenticationManager to perform authentication
//                new UsernamePasswordAuthenticationToken(   //  Create an authentication token object with email & password
//                        request.getEmail(),   //  Set the email (used as username) from the request
//                        request.getPassword() //  Set the raw password from the request
//                )
//        );
//
//
//        // if user is valid, proceed to generate a token and return in response
//        // need to add 3 dependency in pom.xml for jwt
//        // use openssl rand -base64 64 to generate a secret or use below command
//        // [Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
//        // add dependency for spring dotenv
//        // create env file to store the key and  keep secret out of project files
//        // and make sure, this file name in .gitignore
//        // and refer it in application.yml
//        // and use value annotation in JwtService to retrieve that value
//
////        var token = jwtService.generateToken(request.getEmail());
//
//        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
//        var token = jwtService.generateAccessToken(user);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletResponse response
    ){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshtoken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); //7d (same as refresh token expiry)
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ){
        if(!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwtService.getUserIdFromToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok().body(new JwtResponse(accessToken));
    }

    // just a endpoint to validate token
    @PostMapping("/auth/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    // But the right way to validate token is using filter
    /**
     * Filter - A class that runs before controllers and
     *          can intercept(inspect or modify) HTTP request
     * Used for -> Check for authentication, Log incoming request
     *              Modify header, Block suspicious traffic
     * Spring security uses a chain of filters and each filter do specific job
     */

    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> me(){

//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var email = (String) authentication.getPrincipal();
        /**
         * Since finding the user by email is slower than
         * finding by userId (it is faster because of primary key and indexed)
         * we can change the token sub to id from email
         * .subject(user.getId().toString()) in JwtService class
         * and create getUserIdFromToken() method in JwtService class
         * and use this method in JwtAuthenticationFilter
         *
         * var authentication = new UsernamePasswordAuthenticationToken(
         *                 jwtService.getUserIdFromToken(token), // Principal (here: userId from token)
         *                 null,                                // No credentials (already validated by JWT)
         *                 null                                 // No authorities/roles for now
         *         );
         */
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var userId = (Long) authentication.getPrincipal();
//
//        var user = userRepository.findById(userId).orElse(null);
        var user = authService.getCurrentUser();
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        var userdto = userMapper.toDto(user);
        return ResponseEntity.ok(userdto);
    }


}


