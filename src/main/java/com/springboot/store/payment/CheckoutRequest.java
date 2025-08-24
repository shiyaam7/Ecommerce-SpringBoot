package com.springboot.store.payment;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {

    @NotNull(message = "CartID is required")
    private UUID cartID;
}
