package com.example.gateway.presentation.controllers;

import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.controllers.handlers.MailHandler;
import com.example.gateway.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController(value = "mail_controller")
@RequestMapping(Endpoints.GATEWAY_PREFIX)
@Slf4j
public class MailController {
    private final MailHandler mailHandler;

    @Autowired
    public MailController(IMailService mailService) {
        this.mailHandler = new MailHandler(mailService);
    }

    @PostMapping(Endpoints.SEND_CONFIRMATION_MAIL)
    public Mono<ResponseEntity<CustomResponse<Object>>> sendConfirmationMail(
            @RequestBody MailRequest mailRequest
    ) {
        log.info("Received request to send confirmation mail.");
        return mailHandler.sendConfirmationMail(mailRequest)
                .doOnSuccess(responseEntity -> log.info("Confirmation mail sent successfully. Response: {}", responseEntity.getBody()))
                .doOnError(error -> log.error("Error sending confirmation mail. Error: {}", error.getMessage()));
    }

    @PostMapping(Endpoints.SEND_REGULAR_MAIL)
    public Mono<ResponseEntity<CustomResponse<Object>>> sendRegularMail(
            @RequestBody MailRequest mailRequest
    ) {
        log.info("Received request to send regular mail.");
        return mailHandler.sendRegularMail(mailRequest)
                .doOnSuccess(responseEntity -> log.info("Regular mail sent successfully. Response: {}", responseEntity.getBody()))
                .doOnError(error -> log.error("Error sending regular mail. Error: {}", error.getMessage()));
    }
}
