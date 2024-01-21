package com.example.gateway.dtos.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomResponse<T> {
    @JsonProperty("timestamp")
    private final String timestamp;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("payload")
    private final T payload;

    public CustomResponse() {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.message = "";
        this.payload = null;
    }

    public CustomResponse(String message) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.message = message;
        this.payload = null;
    }

    public CustomResponse(String message, T payload) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.message = message;
        this.payload = payload;
    }
}