package com.example.ordertakerservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    @Value("${kafka.order-request.topic}")
    private String orderRequestTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ConcurrentHashMap<String, CompletableFuture<OrderResult>> orderFutures = new ConcurrentHashMap<>();

    public CompletableFuture<OrderResult> submitOrder(final OrderRequest request) {

        var future = new CompletableFuture<OrderResult>();
        orderFutures.put(request.ticketId(), future);

        kafkaTemplate.send(orderRequestTopic, request);

        return future;
    }

    @KafkaListener(
            topics = "${kafka.order-result.topic}",
            properties = {"spring.json.value.default.type=com.example.ordertakerservice.OrderProducer.OrderResult"},
            concurrency = "${kafka.order-result.concurrency}"
    )
    public void listenOrderResult(@Payload final OrderResult result) {
        log.info("Received a order result: {}", result);

        var future = orderFutures.remove(result.ticketId());
        if (null != future) {
            future.complete(result);
        }
    }

    public record OrderRequest(String ticketId) {
    }

    public record OrderResult(String ticketId, String status) {
    }
}