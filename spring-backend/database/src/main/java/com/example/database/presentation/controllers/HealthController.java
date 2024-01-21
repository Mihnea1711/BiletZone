package com.example.database.presentation.controllers;

import com.example.database.dtos.responses.CustomResponse;
import com.example.database.utils.Constants;
import com.example.database.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController("health_controller")
@RequestMapping(Constants.DATABASE_PREFIX)
@Slf4j
public class HealthController {
    @GetMapping(Endpoints.HEALTH_ENDPOINT)
    public ResponseEntity<CustomResponse<String>> healthCheck() {
        log.info("Health check requested");

        String message = "Health Check OK";
        CustomResponse<String> response = new CustomResponse<>(
                message,
                null
        );

        log.info("Health check response: {}", response);
        return ResponseEntity.ok(response);
    }
}
