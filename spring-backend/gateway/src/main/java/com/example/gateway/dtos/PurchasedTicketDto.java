package com.example.gateway.dtos;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record PurchasedTicketDto(
        Long purchaseId,
        @NotNull(message = "Ticket ID cannot be null") Long ticketId,
        @NotNull(message = "User UUID cannot be null") String userUUID,
        @NotNull(message = "Quantity cannot be null") Integer quantity,

        EventDto eventDto
) implements Serializable {
}