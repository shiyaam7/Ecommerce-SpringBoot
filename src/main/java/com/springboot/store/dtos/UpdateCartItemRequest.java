package com.springboot.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided")
    @PositiveOrZero
    private Integer quantity;
}
