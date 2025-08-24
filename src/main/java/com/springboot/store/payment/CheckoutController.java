package com.springboot.store.payment;

import com.springboot.store.dtos.ErrorDto;
import com.springboot.store.repositories.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//
//@RestController
//@AllArgsConstructor
//public class CheckoutController {
//
//    private final CartRepository cartRepository;
//    private final AuthService authService;
//    private final OrderRepository orderRepository;
//    private final CartService cartService;
//
//    @PostMapping("/checkout")
//    public ResponseEntity<?> checkout(
//            @Valid @RequestBody CheckoutRequest request
//            ){
//
//        var cart = cartRepository.getCartWithItems(request.getCartID()).orElse(null);
//        if(cart == null){
//            return ResponseEntity.badRequest().body(
////                    Map.of("error", "Cart not found")
//                    new ErrorDto("Cart not found")
//            );
//        }
//
//        if(cart.getCartItems().isEmpty()){
//            return ResponseEntity.badRequest().body(
////                    Map.of("error", "Cart is empty")
//                    new ErrorDto("Cart not found")
//            );
//        }
//
////        var order = new Order();
////
////        order.setUser(authService.getCurrentUser());
////        order.setStatus(OrderStatus.PENDING);
////        order.setTotalPrice(cart.getTotalPrice());
////
////        cart.getCartItems().forEach(item -> {
////           var orderItem = new OrderItem();
////           orderItem.setOrder(order);
////           orderItem.setProduct(item.getProduct());
////           orderItem.setUnitPrice(item.getProduct().getPrice());
////           orderItem.setTotalPrice(item.getTotalPrice());
////           orderItem.setQuantity(item.getQuantity());
////           order.getItems().add(orderItem);
////        });
//
//        var order = Order.fromCart(cart, authService.getCurrentUser());
//
//       orderRepository.save(order);
//       cartService.clearCart(cart.getId());
//
//       return ResponseEntity.ok(new CheckoutResponse(order.getId()));
//    }
//}
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }

}