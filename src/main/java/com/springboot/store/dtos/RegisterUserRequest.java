package com.springboot.store.dtos;

import com.springboot.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// @Data - combination of @Getter + @Setter + @RequiredArgsConstructor + @ToString +  @EqualsAndHashCod
@Data
public class RegisterUserRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid one")
    @Lowercase
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 15, message = "Password must be between 6 to 25 characters")
    private String password;
}
