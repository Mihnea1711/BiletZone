package com.example.gateway.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for Profile
 */
public record ProfileDto(
        Long id,
        @NotNull(message = "First Name cannot be null") @NotEmpty(message = "First Name cannot be empty") String firstName,
        @NotNull(message = "Last Name cannot be null") @NotEmpty(message = "Last Name cannot be empty") String lastName,
        @NotNull(message = "Phone Number cannot be null") @NotEmpty(message = "Phone Number cannot be empty") String phoneNumber,
        @NotNull(message = "UserUUID cannot be null") @NotEmpty(message = "UserUUID cannot be empty") String userUUID
    ) implements Serializable {
}