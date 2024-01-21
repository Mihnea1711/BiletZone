package com.example.gateway.presentation.controllers;

import com.example.gateway.business.interfaces.IEventService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.requests.FavouriteRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.controllers.handlers.EventHandler;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController(value = "event_controller")
@RequestMapping(Endpoints.GATEWAY_PREFIX)
@Slf4j
public class EventController {
    private final EventHandler eventHandler;

    @Autowired
    public EventController(IEventService eventService) {
        this.eventHandler = new EventHandler(eventService);
    }

    @GetMapping(Endpoints.GET_EVENTS)
    public Mono<ResponseEntity<CustomResponse<List<EventDto>>>> getEvents(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "before_than", required = false) String beforeThan,
            @RequestParam(name = "after_than", required = false) String afterThan,
            @RequestParam(name = "event_type", required = false) String eventType,
            Pageable pageable
    ) {
        String rawQuery = Utils.BuildRawQuery(name, city, beforeThan, afterThan, eventType, pageable);
        log.info("Received request to get events with query: {}", rawQuery);
        return eventHandler.getAllEvents(rawQuery)
                .doOnSuccess(responseEntity -> log.info("Events retrieved successfully. Response: {}", responseEntity.getBody()))
                .doOnError(error -> log.error("Error retrieving events. Error: {}", error.getMessage()));
    }
    @DeleteMapping(Endpoints.DELETE_EVENT)
    public Mono<ResponseEntity<CustomResponse<Object>>> deleteEvent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                      @PathVariable Long id) {
        return eventHandler.deleteEvent(id);
    }
}
