package com.example.orderprocessorservice;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaOrderTakerConsumer {

    @KafkaListener(
            topics = "${kafka.order-taker.topic}",
            properties = {"spring.json.value.default.type=com.example.orderprocessorservice.KafkaOrderTakerConsumer.OrderTakerRequest"},
            concurrency = "${kafka.order-taker.concurrency}"
    )
    @SneakyThrows
    public void listenTransactions(@Payload final OrderTakerRequest request) {
        log.info("Received a order taker request: {}", request);
    }

    public record OrderTakerRequest(String ticketId) {
    }
}

