package com.springboot.store.repositories;

import com.springboot.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @EntityGraph(attributePaths = "cartItems.product")
    @Query("select c from Cart c where c.id = :cartid")
    Optional<Cart> getCartWithItems(UUID cartid);
}