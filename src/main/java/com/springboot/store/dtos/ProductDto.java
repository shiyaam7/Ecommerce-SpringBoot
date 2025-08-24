package com.springboot.store.dtos;

import com.springboot.store.entities.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Byte categoryId;
}
