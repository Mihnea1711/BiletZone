package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.requests.FavouriteRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IEventService {
    Mono<CustomResponse<List<EventDto>>> getAllEvents(String rawQuery);
    Mono<CustomResponse<EventDto>> toggleFavourite(Long eventID, FavouriteRequest favouriteRequest);
    Mono<CustomResponse<Object>> deleteEvent(Long id);

}
