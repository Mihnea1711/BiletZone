package com.example.mail.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
    @NotNull(message = "Destination Email cannot be null")
    @NotEmpty(message = "Destination Email cannot be empty")
    private String to;

    @NotNull(message = "Email Subject cannot be null")
    @NotEmpty(message = "Email Subject cannot be empty")
    private String subject;

    @NotNull(message = "Email Message cannot be null")
    @NotEmpty(message = "Email Message cannot be empty")
    private String message;
}
