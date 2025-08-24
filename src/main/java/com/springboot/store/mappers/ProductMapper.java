package com.springboot.store.mappers;

import com.springboot.store.dtos.ProductDto;
import com.springboot.store.dtos.RegisterProductRequest;
import com.springboot.store.dtos.UpdateUserRequest;
import com.springboot.store.entities.Product;
import com.springboot.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    Product toEntity(RegisterProductRequest request);

    @Mapping(target = "id", ignore = true) // don't change the ID
    void updateProductFromDto(RegisterProductRequest dto, @MappingTarget Product entity);
}
