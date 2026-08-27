package com.practice.order_service.dto;

public class OrderResponse {

    private Long id;
    private Long productId;
    private Integer quantity;
    private String status;

    public OrderResponse(Long id, Long productId, Integer quantity, String status) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
