//package com.springboot.store.controllers;
//
//import com.springboot.store.dtos.AddItemToCartRequest;
//import com.springboot.store.dtos.CartDto;
//import com.springboot.store.dtos.CartItemDto;
//import com.springboot.store.dtos.UpdateCartItemRequest;
//import com.springboot.store.entities.Cart;
//import com.springboot.store.entities.CartItem;
//import com.springboot.store.mappers.CartMapper;
//import com.springboot.store.repositories.CartItemRepository;
//import com.springboot.store.repositories.CartRepository;
//import com.springboot.store.repositories.ProductRepository;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Map;
//import java.util.UUID;
//
//@AllArgsConstructor
//@RestController
//public class CartController {
//
//    private final CartRepository cartRepository;
//    private final CartMapper cartMapper;
//    private final ProductRepository productRepository;
//    private final CartItemRepository cartItemRepository;
//
//    @PostMapping("/carts")
//    public ResponseEntity<CartDto> createCart(
//            UriComponentsBuilder uriBuilder
//    ){
//        var cart = new Cart();
//        cartRepository.save(cart);
//
//        var cartDto = cartMapper.toDto(cart);
//
//        var uri = uriBuilder
//                .path("/carts/{id}")
//                .buildAndExpand(cartDto.getId())
//                .toUri();
//
//        return ResponseEntity.created(uri).body(cartDto);
//    }
//
//    @PostMapping("/carts/{cartId}/items")
//    public ResponseEntity<CartItemDto> addToCart(
//            @PathVariable UUID cartId,
//            @Valid @RequestBody AddItemToCartRequest request
//    ){
////        var cart = cartRepository.findById(cartId).orElse(null);
//        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
//        if(cart == null){
//            return ResponseEntity.notFound().build();
//        }
//
//        var product = productRepository.findById(request.getProductId()).orElse(null);
//        if(product == null){
//            return ResponseEntity.badRequest().build();
//        }
//
////        var cartItem = cart.getCartItems().stream()
////                .filter(item -> item.getProduct().getId().equals(product.getId()))
////                .findFirst()
////                .orElse(null);
//
////        var cartItem = cart.getItem(product.getId());
//
////        if(cartItem != null){
////            cartItem.setQuantity(cartItem.getQuantity() + 1);
////        }
////        else{
////            cartItem = new CartItem();
////            cartItem.setProduct(product);
////            cartItem.setQuantity(1);
////            cartItem.setCart(cart);
////            cart.getCartItems().add(cartItem);
////        }
//
//        CartItem cartItem = cart.addItem(product);
//
//        /**
//         * PERSIST → when you save a new parent, also save its children.
//         *
//         * MERGE → when you update (re-attach) a parent, also update its children.
//         */
//        cartRepository.save(cart);
//
//        var cartItemDto = cartMapper.toDto(cartItem);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
//
//    }
//
//    @GetMapping("/carts/{cartID}")
//    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartID){
////        var cart = cartRepository.findById(cartID).orElse(null);
//        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
//        if(cart == null){
//            return ResponseEntity.notFound().build();
//        }
//        CartDto cartdto = cartMapper.toDto(cart);
//        return ResponseEntity.ok(cartdto);
//    }
//
//    @PutMapping("/carts/{cartID}/items/{productID}")
//    public ResponseEntity<?> updateItem(
//            @PathVariable UUID cartID,
//            @PathVariable Long productID,
//            @Valid @RequestBody UpdateCartItemRequest request
//            ){
//        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
//        if(cart == null){
////            return ResponseEntity.notFound().build();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    Map.of("error", "Cart not found")
//            );
//        }
//
////        var product = productRepository.findById(productID).orElse(null);
////        if(product == null){
////            return ResponseEntity.notFound().build();
////        }
//
////        var cartItem = cart.getCartItems().stream()
////                .filter(item -> item.getProduct().getId().equals(productID))
////                .findFirst()
////                .orElse(null);
//
//        // above 4 lines move to Cart entity
//        // Information Expert Principle
//        var cartItem = cart.getItem(productID);
//
//        if(cartItem == null){
////            return ResponseEntity.notFound().build();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    Map.of("error", "Product was not found in the cart")
//            );
//        }
//
//        cartItem.setQuantity(request.getQuantity());
//
////        if (request.getQuantity() == 0) {
////            cart.getCartItems().remove(cartItem);
////            cartItemRepository.delete(cartItem);
////        } else {
////            cartItem.setQuantity(request.getQuantity());
////        }
//
//        cartRepository.save(cart); // aggregate root
//
//        CartItemDto cartitemdto = cartMapper.toDto(cartItem);
//        return ResponseEntity.ok(cartitemdto);
//    }
//
//    @DeleteMapping("/carts/{cartID}/items/{productID}")
//    public ResponseEntity<?> deleteItem(
//            @PathVariable UUID cartID,
//            @PathVariable long productID
//    ){
//        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
//        if(cart == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    Map.of("error", "Cart not found")
//            );
//        }
//
//        cart.removeItem(productID);
//
//        cartRepository.save(cart);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/carts/{cartID}/items")
//    public ResponseEntity<?> clearCart(
//            @PathVariable UUID cartID
//    ){
//        var cart = cartRepository.getCartWithItems(cartID).orElse(null);
//        if(cart == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    Map.of("error", "Cart not found")
//            );
//        }
//
//        cart.clear();
//        cartRepository.save(cart);
//
//        return ResponseEntity.noContent().build();
//    }
//
//
//}

/**
 * Rewriting the Controller class
 * by excluding business logics and put it in Service layer
 * Controller class now only handles request and responses
 */

package com.springboot.store.controllers;

import com.springboot.store.dtos.AddItemToCartRequest;
import com.springboot.store.dtos.CartDto;
import com.springboot.store.dtos.CartItemDto;
import com.springboot.store.dtos.UpdateCartItemRequest;
import com.springboot.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@Tag(name = "Cart API", description = "Operations related to shopping carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        CartDto cartDto = cartService.createCart();
        var uri = uriBuilder
                .path("/carts/{id}")
                .buildAndExpand(cartDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }


    @PostMapping("/carts/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ){
        CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }


    @GetMapping("/carts/{cartID}")
    public CartDto getCart(@PathVariable UUID cartID){
        CartDto cartdto = cartService.getCart(cartID);
//        return ResponseEntity.ok(cartdto);
        return cartdto;
    }


    @PutMapping("/carts/{cartID}/items/{productID}")
    public CartItemDto updateItem(
            @PathVariable UUID cartID,
            @PathVariable Long productID,
            @Valid @RequestBody UpdateCartItemRequest request
    ){
        CartItemDto cartitemdto = cartService.updateItem(cartID, productID, request.getQuantity());
//        return ResponseEntity.ok(cartitemdto);
        return cartitemdto;
    }



    @DeleteMapping("/carts/{cartID}/items/{productID}")
    public ResponseEntity<?> deleteItem(
            @PathVariable UUID cartID,
            @PathVariable long productID
    ){
        cartService.deleteItem(cartID, productID);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "",
            description = ""
    )
    @DeleteMapping("/carts/{cartID}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable UUID cartID
    ){
        cartService.clearCart(cartID);
        return ResponseEntity.noContent().build();
    }

}

