package com.springboot.store.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LowercaseValidator.class) // link to validator
public @interface Lowercase {
    String message() default "Must be lowercase";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

