package com.springboot.store.services;

import com.springboot.store.dtos.OrderDto;
import com.springboot.store.exceptions.OrderNotFoundException;
import com.springboot.store.exceptions.AccessDeniedException;
import com.springboot.store.mappers.OrderMapper;
import com.springboot.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getAllOrderByUser(user);
        return orders.stream()
                .map(order -> orderMapper.toDto(order))
                .toList();

    }

    public OrderDto getOrderById(Long id) {
//        var order = orderRepository.findById(id).orElse(null);
        var order = orderRepository.getOrderWithItems(id)
                .orElseThrow(() -> new OrderNotFoundException());

        var user = authService.getCurrentUser();

        if(!order.isPlacedBy(user)){
            throw new AccessDeniedException();
        }

        return orderMapper.toDto(order);
    }
}
