package com.springboot.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return cartItems.stream()
                .map(item -> item.getTotalPrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    public CartItem getItem(Long productID){
        return cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productID))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product){
        var cartItem = getItem(product.getId());
        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId){
        var cartItem = getItem(productId);
        if(cartItem != null){
            cartItems.remove(cartItem);
            // since null is not acceptable in the db field of cart
            // we need to mention orphanRemoval = true
            // in the relationship cart -> cartItem
            cartItem.setCart(null);
        }
    }

    public void clear(){
        cartItems.clear();
    }

    public boolean isEmpty(){
        return cartItems.isEmpty();
    }

}
