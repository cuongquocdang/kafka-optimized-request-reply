package com.example.ordertakerservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderProducer orderProducer;

    @PostMapping
    public CompletableFuture<OrderProducer.OrderResult> placeOrder(@RequestBody final OrderProducer.OrderRequest request) {
        return orderProducer.submitOrder(request);
    }
}