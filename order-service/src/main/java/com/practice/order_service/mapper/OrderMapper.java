package com.practice.order_service.mapper;

import com.practice.order_service.dto.OrderRequest;
import com.practice.order_service.dto.OrderResponse;
import com.practice.order_service.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public Order toEntity(OrderRequest request) {

      Order order = new Order();
      order.setProductId(request.getProductId());
      order.setQuantity(request.getQuantity());
      return order;
  }

  public OrderResponse toResponse(Order order) {

      return new OrderResponse(order.getId(),
              order.getProductId(),
              order.getQuantity(),
              order.getStatus());
  }
}
