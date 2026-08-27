package com.practice.order_service.controller;

import com.practice.order_service.dto.OrderRequest;
import com.practice.order_service.dto.OrderResponse;
import com.practice.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
      this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
      return orderService.getAllOrders();
    }

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest){
      return orderService.createOrder(orderRequest);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping
    public OrderResponse updateOrder(@PathVariable Long id,
                                    @Valid @RequestBody OrderRequest request) {
        return orderService.updateOrder(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

}
