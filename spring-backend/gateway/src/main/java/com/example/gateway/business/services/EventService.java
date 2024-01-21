package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IEventService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.requests.FavouriteRequest;
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
public class EventService implements IEventService {
    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public EventService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<CustomResponse<List<EventDto>>> getAllEvents(String rawQuery) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.DATABASE_GET_ALL_EVENTS, rawQuery);

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<EventDto>> toggleFavourite(Long eventID, FavouriteRequest favouriteRequest) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.DATABASE_TOGGLE_FAVOURITE.apply(eventID.toString()), "");

        return webClient
                .patch()
                .uri(uri)
                .body(BodyInserters.fromValue(favouriteRequest))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> deleteEvent(Long id) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String URL = Endpoints.DATABASE_DELETE_EVENT.apply(id.toString());

        return webClient
                .delete()
                .uri(Utils.BuildEndpoint(host, port, URL, ""))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

}
