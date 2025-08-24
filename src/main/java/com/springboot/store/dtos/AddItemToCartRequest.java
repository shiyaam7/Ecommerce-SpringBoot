package com.springboot.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {

    @NotNull(message = "ProductId should not be null")
    private Long productId;
}
