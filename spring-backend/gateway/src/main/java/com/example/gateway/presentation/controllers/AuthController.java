package com.example.gateway.presentation.controllers;

import com.example.gateway.business.interfaces.IAuthService;
import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.dtos.requests.AuthRequest;
import com.example.gateway.dtos.requests.ConfirmationRequest;
import com.example.gateway.dtos.requests.RegisterRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.controllers.handlers.UserHandler;
import com.example.gateway.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController(value = "auth_controller")
@RequestMapping(Endpoints.GATEWAY_PREFIX)
@Slf4j
public class AuthController {
    private final UserHandler userHandler;

    @Autowired
    public AuthController(IAuthService gatewayService, IProfileService profileService, IMailService mailService) {
        this.userHandler = new UserHandler(gatewayService, profileService, mailService);
    }

    @PostMapping(Endpoints.REGISTER_ENDPOINT)
    public Mono<ResponseEntity<CustomResponse<Object>>> registerUser(@RequestBody RegisterRequest registerRequest) {
        log.info("Received request to register user.");
        return userHandler.handleRegistration(registerRequest);
    }

    @PostMapping(Endpoints.LOGIN_ENDPOINT)
    public Mono<ResponseEntity<CustomResponse<String>>> loginUser(@RequestBody AuthRequest authRequest) {
        log.info("Received request to login user.");
        return userHandler.handleLogin(authRequest);
    }

    @PatchMapping(Endpoints.CONFIRM_EMAIL)
    public Mono<ResponseEntity<CustomResponse<Object>>> confirmUserEmail(@RequestBody ConfirmationRequest confirmationRequest) {
        log.info("Received request to confirm user email.");
        return userHandler.confirmMail(confirmationRequest);
    }

    @DeleteMapping(Endpoints.DELETE_USER + "{uuid}")
    public Mono<ResponseEntity<CustomResponse<Void>>> deleteUser(@PathVariable String uuid) {
        return userHandler.deleteUser(uuid);
    }
}
