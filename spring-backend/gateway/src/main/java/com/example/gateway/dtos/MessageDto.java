package com.example.gateway.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record MessageDto (
        Long id,
        //@NotNull(message = "EventID cannot be null") @NotEmpty(message = "EventID cannot be empty") EventDto eventId,
        @NotNull(message = "UserUUID cannot be null") @NotEmpty(message = "UserUUID cannot be empty") String userUUID,
        @NotNull(message = "Text cannot be null") @NotEmpty(message = "Text cannot be empty") String messageText

) implements Serializable {}