package com.springboot.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegisterProductRequest {
    private String name;
    private BigDecimal price;
    private String description;
    private Byte categoryId;
}
