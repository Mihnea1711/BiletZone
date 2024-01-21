package com.example.gateway.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfirmationRequest {
    @NotNull(message = "ConfirmationToken cannot be null")
    @NotEmpty(message = "ConfirmationToken cannot be empty")
    private String confirmationToken;
}
