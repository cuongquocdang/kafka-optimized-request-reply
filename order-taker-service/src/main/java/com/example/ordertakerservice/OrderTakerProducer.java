package com.example.ordertakerservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTakerProducer {

    @Value("${kafka.order-taker}")
    private String orderTakerTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(final OrderRestController.OrderTakerRequest request) {
        kafkaTemplate.send(orderTakerTopic, request);
    }
}