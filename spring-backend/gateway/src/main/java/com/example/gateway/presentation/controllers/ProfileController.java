package com.example.gateway.presentation.controllers;

import com.example.gateway.business.interfaces.IEventService;
import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.business.services.ProfileService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.MessageDto;
import com.example.gateway.dtos.ProfileDto;
import com.example.gateway.dtos.PurchasedTicketDto;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.controllers.handlers.EventHandler;
import com.example.gateway.presentation.controllers.handlers.ProfileHandler;
import com.example.gateway.utils.Constants;
import com.example.gateway.utils.Endpoints;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.gateway.utils.JWT.JWT_SECRET;
import static com.example.gateway.utils.Utils.ExtractJwtToken;
import static com.example.gateway.utils.Utils.ExtractSubFromJWT;

@CrossOrigin(origins="http://localhost:4200")
@RestController(value = "profile_controller")
@RequestMapping(Endpoints.GATEWAY_PREFIX)
@Slf4j
public class ProfileController {
    private final ProfileHandler _profileHandler;

    @Autowired
    public ProfileController(IProfileService profileService) {
        this._profileHandler = new ProfileHandler(profileService);
    }

    @GetMapping(Endpoints.GET_PROFILE)
    public Mono<ResponseEntity<CustomResponse<ProfileDto>>> getProfile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        //String userUUID = ExtractSubFromJWT();
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        System.out.println("Sunt in get profile din profile controller");
        return _profileHandler.getProfile(userUUID);
    }

    @PutMapping(Endpoints.EDIT_PROFILE)
    public Mono<ResponseEntity<CustomResponse<ProfileDto>>> editProfile(

            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody ProfileDto updatedProfileDto
    )
    {
        System.out.println("Sunt aici");
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        return _profileHandler.updateProfile(userUUID, updatedProfileDto);

    }

    @GetMapping(Endpoints.GET_PURCHASED_TICKETS)
    public Mono<ResponseEntity<CustomResponse<List<PurchasedTicketDto>>>> getPurchasedTickets(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        return _profileHandler.getPurchasedTickets(userUUID);
    }

    @GetMapping(Endpoints.GET_FAVORITE_EVENTS)
    public Mono<ResponseEntity<CustomResponse<List<EventDto>>>> getFavoriteEvents(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        return _profileHandler.getFavoriteEvents(userUUID);
    }

    @PostMapping(Endpoints.ADD_EVENT_TO_FAVORITES)
    public Mono<ResponseEntity<CustomResponse<Object>>> addFavoriteEvent(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable String eventID
    ) {
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        return _profileHandler.addFavoriteEvent(eventID, userUUID);
    }

    @DeleteMapping(Endpoints.DELETE_EVENT_FROM_FAVORITES)
    public Mono<ResponseEntity<CustomResponse<Object>>> deleteFavoriteEvent(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable String eventID
    ) {
        String userUUID = ExtractSubFromJWT(authorizationHeader);
        return _profileHandler.deleteFavoriteEvent(eventID, userUUID);
    }

    @GetMapping(Endpoints.GET_ALL_PROFILES)
    public Mono<ResponseEntity<List<ProfileDto>>> getAllProfiles(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
    {
        return _profileHandler.getAllProfiles();
    }

    @DeleteMapping(Endpoints.DELETE_PROFILE)
    public Mono<ResponseEntity<CustomResponse<Object>>> deleteProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id) {
        return _profileHandler.deleteProfile(id);
    }



}
