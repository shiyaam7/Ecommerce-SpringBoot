package com.springboot.store.services;

import com.springboot.store.dtos.CartDto;
import com.springboot.store.dtos.CartItemDto;
import com.springboot.store.entities.Cart;
import com.springboot.store.entities.CartItem;
import com.springboot.store.exceptions.CartNotFoundException;
import com.springboot.store.exceptions.ProductNotFoundException;
import com.springboot.store.mappers.CartMapper;
import com.springboot.store.repositories.CartRepository;
import com.springboot.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }
    
    public CartItemDto addToCart(UUID cartID, long productId){
        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }

        CartItem cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartID){
        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartID, long productID, Integer quantity){
        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productID);
        if(cartItem == null){
           throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart); // aggregate root

        return cartMapper.toDto(cartItem);
    }

    public void deleteItem(UUID cartID, long productID){
        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        cart.removeItem(productID);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartID){
        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }
}
