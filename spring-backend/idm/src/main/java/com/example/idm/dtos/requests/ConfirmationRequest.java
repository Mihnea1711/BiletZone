package com.example.idm.dtos.requests;

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
public class ConfirmationRequest {
    @NotNull(message = "ConfirmationToken cannot be null")
    @NotEmpty(message = "ConfirmationToken cannot be empty")
    private String confirmationToken;
}
