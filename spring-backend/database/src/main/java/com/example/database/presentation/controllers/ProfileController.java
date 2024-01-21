package com.example.database.presentation.controllers;

import com.example.database.business.interfaces.IProfileService;
import com.example.database.dtos.EventDto;
import com.example.database.dtos.ProfileDto;
import com.example.database.dtos.PurchasedTicketDto;
import com.example.database.dtos.requests.CreateProfileRequest;
import com.example.database.dtos.responses.CustomResponse;
import com.example.database.models.Profile;
import com.example.database.models.Event;
import com.example.database.models.Profile;
import com.example.database.utils.Constants;
import com.example.database.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin
@RestController("profile_controller")
@RequestMapping(Constants.DATABASE_PREFIX)
@Slf4j
public class    ProfileController {
    private final IProfileService _profileService;

    @Autowired
    public ProfileController(IProfileService profileService) { this._profileService = profileService; }

    @PostMapping(Endpoints.CREATE_ROUTE)
    public ResponseEntity<CustomResponse<Object>> createAccount(@RequestBody CreateProfileRequest cpr) {
        try {
            ProfileDto profileDto = new ProfileDto(null, cpr.getFirstName(), cpr.getLastName(), cpr.getPhoneNumber(), cpr.getUserUUID());
            this._profileService.createProfile(profileDto);


            // Create a CustomResponse
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Profile created successfully",
                    null
            );

            log.info(String.valueOf(customResponse));
            // Return a response with a 201 (Created) status and the custom response body
            return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
        } catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Validation error",
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);
        } catch (DataIntegrityViolationException e) {
            // Log the duplicate entry error
            log.error("Duplicate entry error: userUUID '{}' already exists. '{}'", cpr.getUserUUID(), e.getMessage());

            // Create a CustomResponse for duplicate entry error
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "userUUID is already present in the db",
                    null
            );

            // Return a response with a 409 (Conflict) status and the custom response body
            return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
        } catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error creating profile: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Error creating profile",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }


    @GetMapping(Endpoints.GET_FAVORITE_EVENTS)
    public ResponseEntity<CustomResponse<List<EventDto>>> getFavoriteEvents(@PathVariable String userUUID) {
        try {
            List<EventDto> events = this._profileService.getFavoriteEvents(userUUID);
            System.out.println(events);
            // Create a CustomResponse
            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    "Favorite events retrieved created successfully",
                    events
            );
            return ResponseEntity.ok(customResponse);

        }
        catch (EmptyResultDataAccessException ex) {
            log.info("No favorite events found");
            String message = "No favorite events found";
            return ResponseEntity.ok(new CustomResponse<>(message, new ArrayList<>()));
        }
        catch (HibernateException | JpaSystemException ex) {
            log.error("A JPA-related error occurred while retrieving favorite events", ex);
            String errorMessage = "A JPA-related error occurred while retrieving favorite events";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
        catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error

            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    "Validation error while retrieving favorite events",
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);


        }
        catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error creating profile: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    "Error retrieving favorite events.",

                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }

    @PostMapping(Endpoints.ADD_FAVORITE_EVENTS)
    public ResponseEntity<CustomResponse<String>> addEventToFavorites(
            @PathVariable Long eventId,
            @PathVariable String userUUID
    ) {
        try {
            System.out.println(eventId);
            System.out.println(userUUID);
            this._profileService.addEventToFavorites(userUUID, eventId);
            return ResponseEntity.ok(new CustomResponse<>("Event added to favorites successfully", null));
        } catch (Exception e) {
            log.error("Error during addEventToFavorites: '{}'", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponse<>(null, "Failed to add event to favorites. Please try again later."));
        }
    }

    @DeleteMapping(Endpoints.DELETE_FAVORITE_EVENTS)
    public ResponseEntity<CustomResponse<String>> removeEventFromFavorites(
            @PathVariable String userUUID,
            @PathVariable Long eventId
    ) {
        try {
            this._profileService.removeEventFromFavorites(userUUID, eventId);
            return ResponseEntity.ok(new CustomResponse<>("Event removed from favorites successfully", null));
        } catch (Exception e) {
            log.error("Error during removeEventFromFavorites: '{}'", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponse<>(null, "Failed to remove event from favorites. Please try again later."));
        }
    }

    @PutMapping(Endpoints.UPDATE_ROUTE+"/{uuid}")
    public ResponseEntity<CustomResponse<Object>> updateProfile(@PathVariable String uuid, @RequestBody ProfileDto updatedProfile) {
        try {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            this._profileService.updateProfile(uuid, updatedProfile);

            // Create a CustomResponse
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Profile updated successfully",
                    null
            );

            log.info(String.valueOf(customResponse));
            // Return a response with a 200 (OK) status and the custom response body
            return ResponseEntity.ok().body(customResponse);
        } catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Validation error",
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);
        } catch (DataIntegrityViolationException e) {
            // Log the duplicate entry error or other data integrity violations
            log.error("Data integrity violation error: '{}'", e.getMessage());

            // Create a CustomResponse for data integrity violation error
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Data integrity violation error",
                    null
            );

            // Return a response with a 409 (Conflict) status and the custom response body
            return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
        } catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error updating profile: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<Object> customResponse = new CustomResponse<>(
                    "Error updating profile",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }

    @GetMapping(Endpoints.GET_PROFILE_ROUTE+"/{uuid}")
    public ResponseEntity<CustomResponse<ProfileDto>> getProfileByUuid(@PathVariable String uuid) {
        System.out.println("Sunt in get profile din profile controller");
        try {
            Optional<ProfileDto> profileOptional = this._profileService.getProfileByUserUUID(uuid);

            if (profileOptional.isPresent()) {
                // Profile found, get the ProfileDto
                ProfileDto profileDto = profileOptional.get();

                // Create a CustomResponse
                CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                        "Profile retrieved successfully",
                        profileDto
                );

                log.info(String.valueOf(customResponse));
                // Return a response with a 200 (OK) status and the custom response body
                return ResponseEntity.ok().body(customResponse);
            } else {
                // Profile not found
                // You might want to return a 404 status code
                log.error("Profile with UUID {} not found.", uuid);
                CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                        "Profile not found",
                        null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customResponse);
            }
        } catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error retrieving profile: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<ProfileDto> customResponse = new CustomResponse<>(
                    "Error retrieving profile",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }

    @GetMapping(Endpoints.GET_ALL_PROFILES)
    public ResponseEntity<List<Profile>> getAllProfiles(){
        List<Profile> profiles = _profileService.getAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @DeleteMapping(Endpoints.DELETE_PROFILE)
    public ResponseEntity<CustomResponse<String>> deleteProfile(
            @PathVariable Long id) {
        try {
            this._profileService.deleteProfile(id);
            return ResponseEntity.ok(new CustomResponse<>("Profile deleted successfully", null));
        } catch (Exception e) {
            log.error("Error during deleteProfile: '{}'", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponse<>(null, "Failed to delete profile. Please try again later."));
        }
    }

    @GetMapping(Endpoints.GET_PURCHASED_TICKETS)
    public ResponseEntity<CustomResponse<List<PurchasedTicketDto>>> getPurchasedTickets(@PathVariable String userUUID) {
        try {
            List<PurchasedTicketDto> purchasedTickets = this._profileService.getPurchasedTickets(userUUID);
            System.out.println(purchasedTickets);

            // Create a CustomResponse
            CustomResponse<List<PurchasedTicketDto>> customResponse = new CustomResponse<>(
                    "Purchased tickets retrieved successfully",
                    purchasedTickets
            );
            return ResponseEntity.ok(customResponse);
        } catch (EmptyResultDataAccessException ex) {
            log.info("No purchased tickets found");
            String message = "No purchased tickets found";
            return ResponseEntity.ok(new CustomResponse<>(message, new ArrayList<>()));
        } catch (HibernateException | JpaSystemException ex) {
            log.error("A JPA-related error occurred while retrieving purchased tickets", ex);
            String errorMessage = "A JPA-related error occurred while retrieving purchased tickets";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        } catch (IllegalArgumentException e) {
            // Log the validation error
            log.error("Validation error: {}", e.getMessage());

            // Create a CustomResponse for validation error
            CustomResponse<List<PurchasedTicketDto>> customResponse = new CustomResponse<>(
                    "Validation error while retrieving purchased tickets",
                    null
            );

            // Return a response with a 400 (Bad Request) status and the custom response body
            return ResponseEntity.badRequest().body(customResponse);
        } catch (Exception e) {
            // Log other exceptions and rethrow them or handle them appropriately
            log.error("Error retrieving purchased tickets: '{}'", e.getMessage());

            // Create a CustomResponse for other errors
            CustomResponse<List<PurchasedTicketDto>> customResponse = new CustomResponse<>(
                    "Error retrieving purchased tickets.",
                    null
            );

            // Return a response with a 500 (Internal Server Error) status and the custom response body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }


}
