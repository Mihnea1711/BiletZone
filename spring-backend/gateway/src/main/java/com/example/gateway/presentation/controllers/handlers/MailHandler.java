package com.example.gateway.presentation.controllers.handlers;

import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class MailHandler {
    private final IMailService _mailService;

    public MailHandler(IMailService mailService) { this._mailService = mailService; }

    public Mono<ResponseEntity<CustomResponse<Object>>> sendConfirmationMail(MailRequest mailRequest) {
        log.info("Handling confirmation mail sending..");
        return _mailService.sendConfirmationMail(mailRequest)
                .map(result -> {
                    CustomResponse<Object> response = new CustomResponse<>(
                            result.getMessage(),
                            null
                    );

                    return ResponseEntity.ok(response);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException while sending confirmation mail: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> response = new CustomResponse<>(
                            "WebClientResponseException during sending confirmation email",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(response));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during sending confirmation mail: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Unexpected error during sending confirmation mail",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Confirmation mail response: {}", response));
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> sendRegularMail(MailRequest mailRequest) {
        log.info("Handling mail sending..");
        return _mailService.sendRegularMail(mailRequest)
                .map(result -> {
                    CustomResponse<Object> response = new CustomResponse<>(
                            result.getMessage(),
                            null
                    );

                    return ResponseEntity.ok(response);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException during sending mail: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> response = new CustomResponse<>(
                            "WebClientResponseException while sending email",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(response));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during sending mail: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Unexpected error during sending mail",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("Mail response: {}", response));
    }
}
