package com.example.database.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record   TicketDto (
        Long id,
        @NotNull(message = "Name cannot be null") @NotEmpty(message = "Name cannot be empty") String name,
        @NotNull(message = "Price cannot be null") @NotEmpty(message = "Price cannot be empty") Double price,
        @NotNull(message = "Quantity cannot be null") @NotEmpty(message = "Quantity cannot be empty") int quantity,
        @NotNull Long eventID
) implements Serializable {
}
