package com.example.database.dtos.responses;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomResponse<T> {
    private final String timestamp;
    private final String message;
    private final T payload;

    public CustomResponse(String message, T payload) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.message = message;
        this.payload = payload;
    }

    // Other constructors or methods as needed
}