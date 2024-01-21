package com.example.database.business.interfaces;

import com.example.database.dtos.EventDto;
import com.example.database.dtos.TicketDto;
import com.example.database.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IEventService {
    public EventDto createEvent(EventDto eventDto);

    Page<EventDto> getAllEvents(String name, String city, String beforeThan, String afterThan, String type, Pageable pageable);

    public Optional<EventDto> findById(Long id);

   void updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

//    EventDto toggleFavourite(Long eventID, boolean isFavourite);
}

