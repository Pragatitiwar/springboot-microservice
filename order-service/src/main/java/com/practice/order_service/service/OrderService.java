package com.practice.order_service.service;

import com.practice.order_service.client.ProductClient;
import com.practice.order_service.dto.OrderRequest;
import com.practice.order_service.dto.OrderResponse;
import com.practice.order_service.dto.ProductResponse;
import com.practice.order_service.entity.Order;
import com.practice.order_service.event.OrderCreatedEvent;
import com.practice.order_service.exception.OrderNotFoundException;
import com.practice.order_service.kafka.OrderEventProducer;
import com.practice.order_service.mapper.OrderMapper;
import com.practice.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductClient productClient, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productClient = productClient;
        this.orderEventProducer = orderEventProducer;
    }

    public OrderResponse createOrder(OrderRequest request) {

        ProductResponse product = productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found with id : " + request.getProductId());
        }

        Order order = orderMapper.toEntity(request);
        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(),
                savedOrder.getProductId(), savedOrder.getQuantity());
        orderEventProducer.publishOrderCreated(event);

        return orderMapper.toResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new OrderNotFoundException("Order not found with id : " + id));

        return orderMapper.toResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new OrderNotFoundException("Order not found with id : " + id));

        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);

    }

    public void deleteOrder(Long id) {

        if(!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id : " + id);
        }
        orderRepository.deleteById(id);
    }
}
