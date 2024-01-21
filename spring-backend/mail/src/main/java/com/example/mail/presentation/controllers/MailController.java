package com.example.mail.presentation.controllers;

import com.example.mail.business.interfaces.IMailService;
import com.example.mail.dtos.requests.MailRequest;
import com.example.mail.dtos.responses.CustomResponse;
import com.example.mail.utils.Constants;
import com.example.mail.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(Constants.MAIL_PREFIX)
@Slf4j
public class MailController {
    private final IMailService _mailService;

    @Autowired
    public MailController(IMailService mailService) { this._mailService = mailService; }

    @GetMapping(Endpoints.HEALTH_ENDPOINT)
    public ResponseEntity<CustomResponse<Object>> healthCheck() {
        log.info("Health check requested");

        String message = "Health check handled";
        CustomResponse<Object> response = new CustomResponse<>(
                message,
                null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(Endpoints.SEND_CONFIRMATION_MAIL_ENDPOINT)
    public ResponseEntity<CustomResponse<Object>> sendConfirmationMail(@RequestBody MailRequest mailRequest) {
        try {
            log.info("Handling confirmation mail sending..");

            this._mailService.sendConfirmationEmail(mailRequest);

            String message = "Confirmation email successfully sent";
            return ResponseEntity.ok(new CustomResponse<>(
                    message,
                    null
            ));
        } catch (Exception e) {
            log.error("Exception caught: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(
                    new CustomResponse<>(
                    "Error: " + e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping(Endpoints.SEND_REGULAR_MAIL_ENDPOINT)
    public ResponseEntity<CustomResponse<Object>> sendMail(@RequestBody MailRequest mailRequest) {
        try {
            log.info("Handling mail sending..");

            this._mailService.sendSimpleEmail(mailRequest);

            String message = "Email successfully sent";
            return ResponseEntity.ok(new CustomResponse<>(
                    message,
                    null
            ));
        } catch (Exception e) {
            log.error("Exception caught: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(
                    new CustomResponse<>(
                            "Error: " + e.getMessage(),
                            null
                    ));
        }
    }
}
