package com.example.gateway.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {User}
 */
public record UserDto(
        @NotNull(message = "UUID cannot be null") @NotEmpty(message = "UUID cannot be empty") String uuid,
        @NotNull(message = "Email cannot be null") @NotEmpty(message = "Email cannot be empty") String email,
        @NotNull(message = "Password cannot be null") @NotEmpty(message = "Password cannot be empty") String password,
        @NotNull(message = "Role cannot be null") @NotEmpty(message = "Role cannot be empty") String role,
        @NotNull(message = "ConfirmationToken cannot be null") String confirmationToken,
        boolean isConfirmed,
        boolean isNotificationsEnabled
) implements Serializable {
}
