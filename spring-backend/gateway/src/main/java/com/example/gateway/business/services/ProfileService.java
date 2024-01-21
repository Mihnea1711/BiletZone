package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.ProfileDto;
import com.example.gateway.dtos.PurchasedTicketDto;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.config.ServiceConfig;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProfileService implements IProfileService {
    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public ProfileService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<CustomResponse<Object>> createProfile(ProfileDto profileDto) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;

        return webClient
                .post()
                .uri(Utils.BuildEndpoint(host, port, Endpoints.DATABASE_CREATE_PROFILE_ENDPOINT, ""))
                .body(BodyInserters.fromValue(profileDto))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<ProfileDto>> getProfile(String userUUID) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_GET_PROFILE.apply(userUUID);

        return webClient
                .get()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomResponse<ProfileDto>>() {});
    }
    @Override
    public Mono<CustomResponse<ProfileDto>> updateProfile(String uuid, ProfileDto updatedProfileDto) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_EDIT_PROFILE.apply(uuid);
        return webClient
                .put()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .body(BodyInserters.fromValue(updatedProfileDto))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomResponse<ProfileDto>>() {});
    }



    @Override
    public Mono<CustomResponse<List<PurchasedTicketDto>>> getPurchasedTickets(String userUUID) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_GET_PURCHASED_TICKETS.apply(userUUID);
        return webClient
                .get()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<List<EventDto>>> getFavoriteEvents(String userUUID) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_GET_FAVORITE_EVENTS.apply(userUUID);
        return webClient
                .get()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> addFavoriteEvent(String eventId, String userUUID) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_ADD_FAVORITE_EVENTS.apply(userUUID, eventId);
        return webClient
                .post()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> deleteFavoriteEvent(String eventId, String userUUID) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_DELETE_FAVORITE_EVENTS.apply(userUUID, eventId);

        return webClient
                .delete()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
    @Override
    public Mono<List<ProfileDto>> getAllProfiles() {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DB_GET_ALL_PROFILES;
        return webClient
                .get()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> deleteProfile(Long id) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_DELETE_PROFILE.apply(id.toString());

        return webClient
                .delete()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<CustomResponse<Void>> deleteUser(String uuid) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port, Endpoints.DATABASE_DELETE_USER.apply(uuid));

        return webClient
                .delete()
                .uri(uri)
                .retrieve()
                .toBodilessEntity()
                .map(responseEntity -> new CustomResponse<>("User was deleted successfully", null));
    }
}
