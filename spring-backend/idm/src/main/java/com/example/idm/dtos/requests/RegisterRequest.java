package com.example.idm.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Role cannot be null")
    @NotEmpty(message = "Role cannot be empty")
    private String role;

    @NotNull(message = "ConfirmationToken cannot be null")
    @NotEmpty(message = "ConfirmationToken cannot be empty")
    private String confirmationToken;

    @NotNull(message = "Notifications flag cannot be null")
    @JsonProperty("isNotificationsEnabled")
    boolean isNotificationsEnabled;
}
