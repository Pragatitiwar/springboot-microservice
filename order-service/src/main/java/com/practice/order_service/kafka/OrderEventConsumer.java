package com.practice.order_service.kafka;

import com.practice.order_service.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(
            topics = "order-events",
            groupId = "order-service-group"
    )
    public void consume(OrderCreatedEvent event) {
        System.out.println("=================================");
        System.out.println("Received OrderCreatedEvent");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Product ID: " + event.getProductId());
        System.out.println("Quantity: " + event.getQuantity());
        System.out.println("=================================");

       /* if (Long.valueOf(6L).equals(event.getOrderId())) {
            throw new RuntimeException("Intentional DLT test failure");
        }*/
    }


}
