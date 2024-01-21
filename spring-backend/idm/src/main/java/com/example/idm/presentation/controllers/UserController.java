package com.example.idm.presentation.controllers;

import com.example.idm.business.interfaces.IUserService;
import com.example.idm.dtos.requests.ConfirmationRequest;
import com.example.idm.dtos.responses.CustomResponse;
import com.example.idm.dtos.UserDto;
import com.example.idm.models.User;
import com.example.idm.dtos.requests.AuthRequest;
import com.example.idm.dtos.requests.RegisterRequest;
import com.example.idm.utils.Constants;
import com.example.idm.utils.Endpoints;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(Constants.IDM_PREFIX)
@Slf4j
public class UserController {
    private final IUserService _userService;

    @Autowired
    public UserController(IUserService userService) { this._userService = userService; }

    // Handle health check to test the server
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

    @PostMapping(Endpoints.REGISTER_ROUTE)
    public ResponseEntity<CustomResponse<String>> registerUser(@RequestBody RegisterRequest rr) {
        try {
            UserDto userDto = new UserDto(null, rr.getEmail(), rr.getPassword(), rr.getRole(), rr.getConfirmationToken(), false, rr.isNotificationsEnabled());

            // maybe track rows affected?
            String userUUID = _userService.createUser(userDto);

            // Create a CustomResponse
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "User created successfully",
                    userUUID
            );

            log.info(String.valueOf(customResponse));
            // Return a response with a 201 (Created) status and the custom response body
            return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
        }
        catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Validation error: " + e.getMessage(),
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);
        }
        catch (DataIntegrityViolationException e) {
            // Log the duplicate entry error
            log.error("Duplicate entry error: Username '{}' already exists. '{}'", rr.getEmail(), e.getMessage());

            // Create a CustomResponse for duplicate entry error
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Email is already registered",
                    null
            );

            // Return a response with a 409 (Conflict) status and the custom response body
            return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
        }
        catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error creating user: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Error creating user",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }

    @PostMapping(Endpoints.LOGIN_ROUTE)
    public ResponseEntity<CustomResponse<String>> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println(authRequest);
            String token = this._userService.loginUser(authRequest.getEmail(), authRequest.getPassword());

            // Create a CustomResponse
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "User authenticated successfully",
                    token
            );

            // Return a response with a 200 (OK) status and the custom response body
            return ResponseEntity.status(HttpStatus.OK).body(customResponse);
        }
        catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Validation error: " + e.getMessage(),
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);
        }
        catch (ResponseStatusException rse) {
            // Log the forbidden exception
            log.error("Response Status exception: {}", rse.getMessage());

            // Create a CustomResponse for forbidden exception
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Account not confirmed. Please check your email for the confirmation link.",
                    null
            );

            // Return a response with a 403 (Forbidden) status and the custom response body
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customResponse);
        }
        catch (EntityNotFoundException enfe) {
            // Log the bad credentials error
            log.error("User not found: {}", enfe.getMessage());

            // Create a CustomResponse for bad credentials
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Invalid username or password",
                    null
            );

            // Return a response with a 401 (Unauthorized) status and the custom response body
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
        }
        catch (BadCredentialsException bce) {
            // Log the bad credentials error
            log.error("Bad credentials error: {}", bce.getMessage());

            // Create a CustomResponse for bad credentials
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Invalid username or password",
                    null
            );

            // Return a response with a 401 (Unauthorized) status and the custom response body
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
        }
        catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error authenticating user: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<String> customResponse = new CustomResponse<>(
                    "Error authenticating user",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = _userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/notified-users")
    public ResponseEntity<List<UserDto>> getUsersForNotification() {
        List<UserDto> users = _userService.getUserForNotifications();
        System.out.println(users);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by ID
    @GetMapping("/users/{userUUID}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userUUID) {
        Optional<UserDto> user = _userService.getUserById(userUUID);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get user by email
    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = _userService.getUserByEmail(email);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update user by ID
    @PutMapping("/users/{userUUID}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID userUUID, @RequestBody User updatedUser) {
        // maybe track rows affected?
        _userService.updateUser(userUUID, updatedUser);
        return null;
    }

    @PatchMapping("/confirm-mail")
    public ResponseEntity<Object> confirmUserEmail(@RequestBody ConfirmationRequest confirmationRequest) {
        UserDto user = this._userService.confirmEmail(confirmationRequest.getConfirmationToken());
        CustomResponse<Object> customResponse = new CustomResponse<>(
                "User email confirmed successfully",
                null
        );

        return ResponseEntity.ok(customResponse);
    }

    // Delete user by ID
    @DeleteMapping("/users/{userUUID}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userUUID) {
        _userService.deleteUser(userUUID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
