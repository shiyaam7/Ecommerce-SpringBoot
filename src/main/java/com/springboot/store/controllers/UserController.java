package com.springboot.store.controllers;

import com.springboot.store.dtos.ChangePasswordRequest;
import com.springboot.store.dtos.RegisterUserRequest;
import com.springboot.store.dtos.UpdateUserRequest;
import com.springboot.store.dtos.UserDto;
import com.springboot.store.entities.Role;
import com.springboot.store.entities.User;
import com.springboot.store.mappers.UserMapper;
import com.springboot.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    @GetMapping("/users")
//    public Iterable<User> getAllUsers(){
//        return userRepository.findAll();
//    }
//
//    @GetMapping("/users/{id}")
//    public ResponseEntity<User> getUser(@PathVariable Long id){
//        var user =  userRepository.findById(id).orElse(null);
//        if(user == null){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);
//    }

    // Using DTO
    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort){

        if(!Set.of("name","email").contains(sort)){
            sort = "name";
        }

        return userRepository.findAll(Sort.by(sort))
                .stream()
//                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
//                .map(user -> userMapper.toDto(user)) // using usermapper
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        var user =  userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
//        var userdto = new UserDto(user.getId(), user.getName(), user.getEmail());
        var userdto = userMapper.toDto(user);
//        return ResponseEntity.ok(userdto);
        // Using usermapper
        return ResponseEntity.ok(userdto);
    }

    // throws MethodArgumentNotValidException when RegisterUserRequest fields details not matches the validation
    @PostMapping("/users")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder)
    {

        // Custom validation of Business Logic
        boolean userExist = userRepository.existsByEmail(request.getEmail());
        if(userExist){
            return ResponseEntity.badRequest().body(
                    Map.of("Email", "Email is already registered")
            );
        }

        User user = userMapper.toEntity(request);

        /**
         * Hash the password before saving - Spring security
         */
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER); // setting role as USER

        userRepository.save(user);
        UserDto userdto = userMapper.toDto(user);

        var uri = uriBuilder
                    .path("/users/{id}")
                    .buildAndExpand(userdto.getId())
                    .toUri();

        return ResponseEntity.created(uri).body(userdto);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            UriComponentsBuilder uriBuilder
            ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        // Update only fields that are not null
//        if(request.getName() != null){
//            user.setName(request.getName());
//        }
//        if(request.getEmail() != null){
//            user.setEmail(request.getEmail());
//        }

        userMapper.updateUserFromDto(request, user);

        userRepository.save(user);

        UserDto userdto = userMapper.toDto(user);

        URI uri = uriBuilder
                .path("/user/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).body(userdto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id){

        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/change-password")
    public ResponseEntity<Void> changePasswordOfUser(
            @PathVariable long id,
            @RequestBody ChangePasswordRequest request
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        if(!request.getOldPassword().equals(user.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }



}
