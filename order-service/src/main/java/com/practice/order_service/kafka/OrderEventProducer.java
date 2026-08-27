package com.practice.order_service.kafka;

import com.practice.order_service.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final String TOPIC = "order-events";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        System.out.println(">>> Publishing order event: " + event.getOrderId());
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
        System.out.println(">>> Kafka send called");
    }
}
