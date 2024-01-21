package com.example.database.presentation.controllers;

import com.example.database.business.interfaces.IEventService;
import com.example.database.dtos.EventDto;
import com.example.database.dtos.TicketDto;
import com.example.database.dtos.responses.CustomResponse;
import com.example.database.utils.Constants;
import com.example.database.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController("event_controller")
@RequestMapping(Constants.DATABASE_PREFIX)
@Slf4j
public class EventController {
    private final IEventService _eventService;

    @Autowired
    public EventController(IEventService eventService) {
        this._eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<CustomResponse<List<EventDto>>> getAllEvents(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "before_than", required = false) String beforeThan,
            @RequestParam(name = "after_than", required = false) String afterThan,
            @RequestParam(name = "event_type", required = false) String eventType,
            Pageable pageable
    ) {
        try {
            Page<EventDto> events = this._eventService.getAllEvents(
                    name,
                    city,
                    beforeThan,
                    afterThan,
                    eventType,
                    pageable
            );

            System.out.println(events.getContent());

            String message = "Retrieved all events successfully";
            CustomResponse<List<EventDto>> response = new CustomResponse<>(message, events.getContent());

            log.info("Retrieved all events successfully");
            return ResponseEntity.ok(response);
        } catch (EmptyResultDataAccessException ex) {
            log.info("No events found");
            String message = "No events found";
            return ResponseEntity.ok(new CustomResponse<>(message, new ArrayList<>()));
        } catch (HibernateException | JpaSystemException ex) {
            log.error("A JPA-related error occurred while retrieving events", ex);
            String errorMessage = "A JPA-related error occurred while retrieving events";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        } catch (Exception ex) {
            log.error("An error occurred while retrieving events", ex);
            // You can customize the error message or response based on the specific exception type
            String errorMessage = "An error occurred while retrieving events";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }

//    @PatchMapping("/events/{eventID}")
//    public ResponseEntity<CustomResponse<EventDto>> toggleFavourite(
//            @PathVariable Long eventID,
//            @RequestBody FavouriteRequest favouriteRequest
//    ) {
//        System.out.println(favouriteRequest);
//        try {
//            EventDto updatedEvent = this._eventService.toggleFavourite(eventID, favouriteRequest.getIsFavourite());
//            if (updatedEvent != null) {
//                String message = "Updated event successfully";
//                CustomResponse<EventDto> response = new CustomResponse<>(message, updatedEvent);
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (EmptyResultDataAccessException ex) {
//            log.info("No events found");
//            String message = "No event found to update";
//            return ResponseEntity.ok(new CustomResponse<>(message, null));
//        } catch (HibernateException | JpaSystemException ex) {
//            log.error("A JPA-related error occurred while retrieving events", ex);
//            String errorMessage = "A JPA-related error occurred while retrieving events";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
//        } catch (ValidationException ex) {
//            // Handle validation errors
//            log.warn("Validation error while updating event with ID {}. {}", eventID, ex.getMessage());
//            return ResponseEntity.badRequest().body(new CustomResponse<>(ex.getMessage(), null));
//        } catch (SecurityException ex) {
//            // Handle security-related issues
//            log.error("Security issue while updating event with ID {}. {}", eventID, ex.getMessage());
//            String errorMessage = String.format("Security issue while updating event with ID %d.", eventID);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomResponse<>(errorMessage, null));
//        } catch (Exception ex) {
//            log.error("An error occurred while retrieving events", ex);
//            // You can customize the error message or response based on the specific exception type
//            String errorMessage = "An error occurred while retrieving events";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
//        }
//    }
@GetMapping("/events/{id}")
public ResponseEntity<CustomResponse<EventDto>> getEventByID(@PathVariable Long id) {
    try {
        Optional<EventDto> event = _eventService.findById(id);
        if (event.isPresent()) {
            String message = "Event retrieved successfully";
            CustomResponse<EventDto> response = new CustomResponse<>(message, event.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            String errorMessage = "Event not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(errorMessage, null));
        }
    } catch (Exception ex) {
        log.error("An error occurred while retrieving the event", ex);
        String errorMessage = "An error occurred while retrieving the event";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
    }
}
    @PostMapping("/events")
    public ResponseEntity<CustomResponse<EventDto>> create(@RequestBody EventDto eventDto) {
        try {
            EventDto createdEvent = _eventService.createEvent(eventDto);
            String message = "Event created successfully";
            CustomResponse<EventDto> response = new CustomResponse<>(message, createdEvent);
            log.info("Event created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ErrorResponseException ex) {
            log.error("An error occurred while creating the event", ex);
            String errorMessage = "Failed to create the event. Please check your input.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse<>(errorMessage, null));
        } catch (Exception ex) {
            log.error("An error occurred while creating the event", ex);
            String errorMessage = "An unexpected error occurred while creating the event";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }
    @DeleteMapping(Endpoints.DELETE_EVENT)
    public ResponseEntity<CustomResponse<Void>> delete(@PathVariable Long id) {
        try {
            Optional<EventDto> event = _eventService.findById(id);
            if (event.isPresent()) {
                _eventService.deleteEvent(id);
                String message = "Event deleted successfully";
                CustomResponse<Void> response = new CustomResponse<>(message, null);
                log.info("Event deleted successfully");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            } else {
                String errorMessage = "Event not found for deletion";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(errorMessage, null));
            }
        } catch (Exception ex) {
            log.error("An error occurred while deleting the event", ex);
            String errorMessage = "An error occurred while deleting the event";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }
    @PutMapping("/events/{id}")
    public ResponseEntity<CustomResponse<Void>> updateEvent(
            @PathVariable Long id,
            @RequestBody EventDto updatedEventDto) {
        try {
            Optional<EventDto> existingEvent = _eventService.findById(id);

            if (existingEvent.isPresent()) {
                 _eventService.updateEvent(id, updatedEventDto);
                String message = "Event updated successfully";
                log.info("Event updated successfully");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                String errorMessage = "Event not found for updating";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(errorMessage, null));
            }
        } catch (Exception ex) {
            log.error("An error occurred while updating the event", ex);
            String errorMessage = "An error occurred while updating the event";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }



}
