package com.example.database.dtos;

import com.example.database.models.PurchasedTicket;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link PurchasedTicket}
 */
public record PurchasedTicketDto(
        Long purchaseId,
        @NotNull(message = "Ticket ID cannot be null") Long ticketId,
        @NotNull(message = "User UUID cannot be null") String userUUID,
        @NotNull(message = "Quantity cannot be null") Integer quantity,

        EventDto eventDto

) {
}
