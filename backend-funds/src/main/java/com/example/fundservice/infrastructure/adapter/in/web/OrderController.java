package com.example.fundservice.infrastructure.adapter.in.web;

import com.example.fundservice.application.port.in.PlaceOrderCommand;
import com.example.fundservice.application.port.in.PlaceOrderUseCase;
import com.example.fundservice.domain.model.Order;
import com.example.fundservice.infrastructure.adapter.in.web.dto.OrderRequest;
import com.example.fundservice.infrastructure.adapter.in.web.dto.OrderResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter exposing the order endpoint under {@code /v1/orders}.
 */
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    /**
     * Creates the controller.
     *
     * @param placeOrderUseCase place-order use case
     */
    public OrderController(final PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    /**
     * Places a new order.
     *
     * @param request validated order payload
     * @return 201 Created with the order summary
     */
    @PostMapping
    public ResponseEntity<OrderResponse> place(@Valid @RequestBody final OrderRequest request) {
        final Order order = placeOrderUseCase.place(new PlaceOrderCommand(
                request.accountId(), request.isin(), request.operationType(),
                request.amount(), request.currency()));
        final OrderResponse response = new OrderResponse(
                order.getOrderId(), order.getStatus().name(), order.getCreatedAt());
        return ResponseEntity.created(URI.create("/v1/orders/" + order.getOrderId())).body(response);
    }
}
