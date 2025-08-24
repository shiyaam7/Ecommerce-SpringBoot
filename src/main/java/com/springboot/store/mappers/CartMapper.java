package com.springboot.store.mappers;

import com.springboot.store.dtos.CartDto;
import com.springboot.store.dtos.CartItemDto;
import com.springboot.store.entities.Cart;
import com.springboot.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.EntityGraph;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalprice", expression = "java(cartItem.getTotalPrice())")
    @Mapping(target = "quantity", source = "quantity")
    CartItemDto toDto(CartItem cartItem);
}
