package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response for a placed order.
 *
 * @param orderId   identifier
 * @param status    lifecycle status
 * @param createdAt creation instant
 */
public record OrderResponse(UUID orderId, String status, Instant createdAt) {
}
