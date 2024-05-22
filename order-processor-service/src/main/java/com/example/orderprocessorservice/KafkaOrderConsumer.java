package com.example.orderprocessorservice;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderConsumer {


    @Value("${kafka.order-result.topic}")
    private String orderResultTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(
            topics = "${kafka.order-request.topic}",
            properties = {"spring.json.value.default.type=com.example.orderprocessorservice.KafkaOrderConsumer.OrderRequest"},
            concurrency = "${kafka.order-request.concurrency}"
    )
    @SneakyThrows
    public void listenTransactions(@Payload final OrderRequest request) {
        log.info("Received a order request: {}", request);

        // dummy handle order
        var orderResult = new OrderResult(request.ticketId(), "ORDERED");
        kafkaTemplate.send(orderResultTopic, orderResult);
    }

    public record OrderRequest(String ticketId) {
    }

    public record OrderResult(String ticketId, String status) {
    }
}

