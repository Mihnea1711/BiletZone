package com.example.gateway.presentation.controllers.handlers;

import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.ProfileDto;
import com.example.gateway.dtos.PurchasedTicketDto;
import com.example.gateway.dtos.responses.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class ProfileHandler {

    private final IProfileService _profileService;

    public ProfileHandler(IProfileService profileService) {
        this._profileService = profileService;
    }


    public Mono<ResponseEntity<CustomResponse<ProfileDto>>> getProfile(String userUUID) {
        return _profileService.getProfile(userUUID)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
            System.out.println(e.getResponseBodyAsString());
            log.error("WebClientResponseException during profile toggle: '{}'", e.getMessage());

            // Extract the HTTP status code from the exception
            int statusCode = e.getStatusCode().value();

            // Create a CustomResponse for WebClientResponseException
            CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                    "Failed to toggle profile. WebClientResponseException occurred.",
                    null
            );

            // Return a response with the original HTTP status code and the custom response body
            return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
        })
                .onErrorResume(Exception.class, e -> {
            log.error("Unexpected error during profile toggle: '{}'", e.getMessage());

            // Create a CustomResponse for other unexpected errors
            CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                    "Failed to toggle profile. Unexpected error occurred.",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
        })
                .doOnSuccess(response -> log.info("Profile toggle completed with status: {}", response.getStatusCode()));

    }

    public Mono<ResponseEntity<CustomResponse<ProfileDto>>> updateProfile(String uuid, ProfileDto updatedProfileDto) {
        return _profileService.updateProfile(uuid, updatedProfileDto)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during profile toggle: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                            "Failed to toggle profile. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during profile toggle: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                            "Failed to toggle profile. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Profile toggle completed with status: {}", response.getStatusCode()));

    }




    public Mono<ResponseEntity<CustomResponse<List<PurchasedTicketDto>>>> getPurchasedTickets(String userUUID) {
        return _profileService.getPurchasedTickets(userUUID)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during purchased tickets retrieval: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<List<PurchasedTicketDto>> customResponse = new CustomResponse<>(
                            "Failed to retrieve purchased tickets. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during purchased tickets retrieval: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<List<PurchasedTicketDto>> customResponse = new CustomResponse<>(
                            "Failed to retrieve purchased tickets. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Purchased tickets retrieval completed with status: {}", response.getStatusCode()));
    }


    public Mono<ResponseEntity<CustomResponse<List<EventDto>>>> getFavoriteEvents(String userUUID) {
        return _profileService.getFavoriteEvents(userUUID)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during event favourite toggle: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                            "Failed to toggle event favourite. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during event favourite toggle: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                            "Failed to toggle event favourite. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Event favourite toggle completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> addFavoriteEvent(String eventId, String userUUID) {
        return _profileService.addFavoriteEvent(eventId, userUUID)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during addFavoriteEvent: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to addFavoriteEvent. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during addFavoriteEvent: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to addFavoriteEvent. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("addFavoriteEvent completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> deleteFavoriteEvent(String eventId, String userUUID) {
        return _profileService.deleteFavoriteEvent(eventId, userUUID)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during deleteFavoriteEvent: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteFavoriteEvent. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during deleteFavoriteEvent: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteFavoriteEvent. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("deleteFavoriteEvent completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<List<ProfileDto>>> getAllProfiles() {
        log.info("Handling profiles retrieval...");

        return this._profileService.getAllProfiles()
                .map(profiles -> {
                    if (profiles.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profiles);
                    } else {
                        return ResponseEntity.ok(profiles);
                    }
                })
                .doOnSuccess(response -> log.info("Profiles retrieval completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> deleteProfile(Long id) {
        return _profileService.deleteProfile(id)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.out.println(e.getResponseBodyAsString());
                    log.error("WebClientResponseException during deleteProfile: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteProfile. WebClientResponseException occurred.",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during deleteProfile: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Failed to deleteProfile. Unexpected error occurred.",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("deleteProfile completed with status: {}", response.getStatusCode()));
    }
}
