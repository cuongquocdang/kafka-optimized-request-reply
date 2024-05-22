package com.example.ordertakerservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderProducer orderProducer;

    @PostMapping
    public void placeOrder(@RequestBody final OrderTakerRequest request) {
        orderProducer.send(request);
    }

    public record OrderTakerRequest(String ticketId) {
    }
}