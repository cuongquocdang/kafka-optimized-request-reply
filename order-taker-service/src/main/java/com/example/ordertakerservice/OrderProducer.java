package com.example.ordertakerservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    @Value("${kafka.order-request.topic}")
    private String orderRequestTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void submitOrder(final OrderRequest request) {
        kafkaTemplate.send(orderRequestTopic, request);
    }

    @KafkaListener(
            topics = "${kafka.order-result.topic}",
            properties = {"spring.json.value.default.type=com.example.ordertakerservice.OrderProducer.OrderResult"},
            concurrency = "${kafka.order-result.concurrency}"
    )
    public void listenOrderResult(@Payload final OrderResult result) {
        log.info("Received a order result: {}", result);
    }

    public record OrderRequest(String ticketId) {
    }

    public record OrderResult(String ticketId, String status) {
    }
}