package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.ProfileDto;
import com.example.gateway.dtos.PurchasedTicketDto;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProfileService {
    Mono<CustomResponse<Object>> createProfile(ProfileDto profileDto);
    Mono<CustomResponse<ProfileDto>> getProfile(String userUUID);
    Mono<CustomResponse<List<EventDto>>> getFavoriteEvents(String userUUID);
    Mono<CustomResponse<Object>> addFavoriteEvent(String eventId, String userUUID);
    Mono<CustomResponse<Object>> deleteFavoriteEvent(String eventId, String userUUID);
    Mono<List<ProfileDto>> getAllProfiles();
    Mono<CustomResponse<Object>> deleteProfile(Long id);
    Mono<CustomResponse<Void>> deleteUser(String uuid);
    Mono<CustomResponse<List<PurchasedTicketDto>>> getPurchasedTickets(String userUUID);
    Mono<CustomResponse<ProfileDto>> updateProfile(String uuid, ProfileDto updatedProfileDto);

}
