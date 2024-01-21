package com.example.gateway.presentation.controllers.handlers;

import com.example.gateway.business.interfaces.IEventService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.requests.FavouriteRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class EventHandler {
    private final IEventService _eventService;

    public EventHandler(IEventService eventService) {
        this._eventService = eventService;
    }

    public Mono<ResponseEntity<CustomResponse<List<EventDto>>>> getAllEvents(String rawQuery) {
        log.info("Handling events retrieval...");

        return this._eventService.getAllEvents(rawQuery)
                .map(eventsResponse -> {
                    log.info("Events retrieved successfully. Response: {}", eventsResponse);

                    // Create a new response entity with the HTTP status code and the original body
                    return ResponseEntity.ok(eventsResponse);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException during events retrieval: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                            "WebClientResponseException during events retrieval",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during events retrieval: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                            "Unexpected error during events retrieval",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Events retrieval completed with status: {}", response.getStatusCode()));
    }
    public Mono<ResponseEntity<CustomResponse<Object>>> deleteEvent(Long id) {
        return _eventService.deleteEvent(id)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during deleteEvent: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteEvent. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during deleteEvent: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteEvent. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("deleteEvent completed with status: {}", response.getStatusCode()));
    }
}
