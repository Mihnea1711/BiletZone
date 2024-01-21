package com.example.database.dtos;

import com.example.database.models.Event;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.example.database.models.Message}
 */
public record MessageDto (
    Long id,
    //@NotNull(message = "EventID cannot be null") @NotEmpty(message = "EventID cannot be empty") Long eventId,
    @NotNull(message = "UserUUID cannot be null") @NotEmpty(message = "UserUUID cannot be empty") String userUUID,
    @NotNull(message = "Text cannot be null") @NotEmpty(message = "Text cannot be empty") String messageText

) implements Serializable{}
