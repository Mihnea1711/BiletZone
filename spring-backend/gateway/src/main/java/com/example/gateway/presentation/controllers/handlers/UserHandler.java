package com.example.gateway.presentation.controllers.handlers;

import com.example.gateway.business.interfaces.IAuthService;
import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.dtos.ProfileDto;
import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.requests.AuthRequest;
import com.example.gateway.dtos.requests.ConfirmationRequest;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.requests.RegisterRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.example.gateway.utils.Constants.ADMIN_REGISTRATION_SUBJECT;
import static com.example.gateway.utils.Constants.ADMIN_ROLE;
import static com.example.gateway.utils.Utils.BuildConfirmationMessage;
import static com.example.gateway.utils.Utils.buildAdminRegistrationMessage;

@Slf4j
public class UserHandler {
    private final IAuthService _authService;
    private final IProfileService _profileService;
    private final IMailService _mailService;

    public UserHandler(IAuthService authService, IProfileService profileService, IMailService mailService) {
        this._authService = authService;
        this._profileService = profileService;
        this._mailService = mailService;
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> handleRegistration(RegisterRequest rr) {
        log.info("Handling user registration...");

        String confirmationToken = TokenGenerator.generateToken();
        boolean isAdmin = Objects.equals(rr.getRole(), ADMIN_ROLE);
        UserDto userDto = new UserDto(null, rr.getEmail(), rr.getPassword(), rr.getRole(), confirmationToken, isAdmin, rr.isNotificationsEnabled());

        return _authService.registerUser(userDto)
                .flatMap(authResponse -> {
                    log.info(authResponse.getMessage());

                    String userUUID = authResponse.getPayload();
                    ProfileDto profileDto = new ProfileDto(0L, rr.getFirstName(), rr.getLastName(), rr.getPhoneNumber(), userUUID);

                    return _profileService.createProfile(profileDto);
                })
                .flatMap(profileResponse -> {
                    log.info("Profile response: {}", profileResponse);

                    if (isAdmin) {
                        String mailMessage = buildAdminRegistrationMessage(rr.getEmail(), rr.getPassword());
                        MailRequest mailRequest = new MailRequest(rr.getEmail(), ADMIN_REGISTRATION_SUBJECT, mailMessage);

                        return this._mailService.sendRegularMail(mailRequest);
                    } else {
                        String mailMessage = BuildConfirmationMessage(confirmationToken);
                        MailRequest mailRequest = new MailRequest(rr.getEmail(), mailMessage);

                        return this._mailService.sendConfirmationMail(mailRequest);
                    }
                })
                .map (confirmationMailResponse -> {
                    log.info("Confirmation mail response: {}", confirmationMailResponse);

                    return ResponseEntity.ok(confirmationMailResponse);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().value() == HttpStatus.CONFLICT.value()) {
                        log.warn("User already exists: {}", e.getResponseBodyAsString());
                        CustomResponse<Object> customResponse = new CustomResponse<>(
                                "Bad registration request. Username already exists.",
                                null
                        );
                        // Return a response with a 409 (Conflict) status and the custom response body
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse));
                    }
                    if (e.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()) {
                        log.error("IllegalArgumentException during user registration: '{}'", e.getMessage());
                        // Create a CustomResponse for IllegalArgumentException
                        CustomResponse<Object> customResponse = new CustomResponse<>(
                                "IllegalArgumentException during user registration",
                                null
                        );
                        // Return a response with a 422 (Unprocessable Entity) status and the custom response body
                        return Mono.just(ResponseEntity.unprocessableEntity().body(customResponse));
                    }
                    log.error("WebClientResponseException during user registration: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "WebClientResponseException during user registration",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during user registration: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Unexpected error during user registration",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("User registration successfully completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<String>>> handleLogin(AuthRequest ar) {
        log.info("Handling user login...");

        return _authService.loginUser(ar)
                .map(response -> {
                    log.info(response.getMessage());
                    // Create a new response entity with the HTTP status code and the original body
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Handle invalid login credentials
                    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        log.warn("Invalid login credentials: {}", e.getResponseBodyAsString());
                        CustomResponse<String> customResponse = new CustomResponse<>(
                                "Invalid login credentials",
                                null
                        );
                        // Return a response with a 401 (Unauthorized) status and the custom response body
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse));
                    }
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        log.error("IllegalArgumentException during user login: '{}'", e.getMessage());
                        // Create a CustomResponse for IllegalArgumentException
                        CustomResponse<String> customResponse = new CustomResponse<>(
                                "IllegalArgumentException during user login",
                                null
                        );
                        // Return a response with a 422 (Unprocessable Entity) status and the custom response body
                        return Mono.just(ResponseEntity.unprocessableEntity().body(customResponse));
                    }
                    else {
                        log.error("WebClientResponseException during user login: '{}'", e.getMessage());

                        // Extract the HTTP status code from the exception
                        int statusCode = e.getStatusCode().value();

                        // Create a CustomResponse for WebClientResponseException
                        CustomResponse<String> customResponse = new CustomResponse<>(
                                "WebClientResponseException during user login",
                                null
                        );

                        // Return a response with the original HTTP status code and the custom response body
                        return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                    }
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during user login: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<String> customResponse = new CustomResponse<>(
                            "Unexpected error during user login",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("User login successfully completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> confirmMail(ConfirmationRequest confirmationRequest) {
        return this._authService.confirmMail(confirmationRequest)
                .map(mailResponse -> {
                    log.info("Profile response: {}", mailResponse);

                    // Create a new response entity with the HTTP status code and the original body
                    return ResponseEntity.ok(mailResponse);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()) {
                        log.error("IllegalArgumentException during user email confirmation: '{}'", e.getMessage());
                        // Create a CustomResponse for IllegalArgumentException
                        CustomResponse<Object> customResponse = new CustomResponse<>(
                                "IllegalArgumentException during user email confirmation",
                                null
                        );
                        // Return a response with a 422 (Unprocessable Entity) status and the custom response body
                        return Mono.just(ResponseEntity.unprocessableEntity().body(customResponse));
                    }
                    log.error("WebClientResponseException during user email confirmation: '{}'", e.getMessage());

                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Create a CustomResponse for WebClientResponseException
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "WebClientResponseException during user email confirmation",
                            null
                    );

                    // Return a response with the original HTTP status code and the custom response body
                    return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during user email confirmation: '{}'", e.getMessage());

                    // Create a CustomResponse for other unexpected errors
                    CustomResponse<Object> customResponse = new CustomResponse<>(
                            "Unexpected error during user email confirmation",
                            null
                    );

                    // Return a response with a 500 (Internal Server Error) status and the custom response body
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse));
                })
                .doOnSuccess(response -> log.info("User email confirmation successfully completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Void>>> deleteUser(String uuid) {
        log.info("Handling message deletion...");

        return _profileService.deleteUser(uuid)
                .map(result -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(result))
                .onErrorResume(e -> handleErrorResponse(e, "WebClientResponseException during message deletion"))
                .doOnSuccess(response -> log.info("Message deletion completed with status: {}", response.getStatusCode()));
    }

    private <T> Mono<ResponseEntity<CustomResponse<T>>> handleErrorResponse(Throwable e, String errorMessage) {
        log.error("{}: '{}'", errorMessage, e.getMessage());
        int statusCode = (e instanceof WebClientResponseException) ?
                ((WebClientResponseException) e).getStatusCode().value() :
                HttpStatus.INTERNAL_SERVER_ERROR.value();

        CustomResponse<T> customResponse = new CustomResponse<>(errorMessage);
        return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
    }
}
