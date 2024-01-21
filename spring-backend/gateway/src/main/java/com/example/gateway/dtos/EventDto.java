package com.example.gateway.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for Event
 */
public record EventDto(
        Long id,
        @NotNull(message = "Name cannot be null") @NotEmpty(message = "Name cannot be empty") String name,
        @NotNull(message = "Description cannot be null") @NotEmpty(message = "Description cannot be empty") String description,
        @NotNull(message = "City cannot be null") @NotEmpty(message = "City cannot be empty") String city,
        @NotNull(message = "Location cannot be null") @NotEmpty(message = "Location cannot be empty") String location,
        @NotNull(message = "Event Type cannot be null") @NotEmpty(message = "Event Type cannot be empty") String type,
        @NotNull(message = "Date cannot be null") @NotEmpty(message = "Date cannot be empty") String date,
        @NotNull(message = "Image cannot be null") @NotEmpty(message = "Image cannot be empty") String image,
        @NotNull HallMapDto hallMapID
    ) implements Serializable {
}