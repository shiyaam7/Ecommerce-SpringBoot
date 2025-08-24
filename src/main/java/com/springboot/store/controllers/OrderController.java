package com.springboot.store.controllers;


import com.springboot.store.dtos.OrderDto;
import com.springboot.store.mappers.OrderMapper;
import com.springboot.store.repositories.OrderRepository;
import com.springboot.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{orderID}")
    public ResponseEntity<?> getOrder(
            @PathVariable(name = "orderID") Long id
    ){
        var orderdto = orderService.getOrderById(id);
        return ResponseEntity.ok().body(orderdto);
    }
}
