package com.example.database.business.services;

import com.example.database.business.interfaces.IEventService;
import com.example.database.dtos.EventDto;
import com.example.database.models.Event;
import com.example.database.persistence.interfaces.IEventRepository;
import com.example.database.persistence.mappers.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class EventService implements IEventService {
    private final IEventRepository _eventRepository;
    private final EventMapper _eventMapper;

    @Autowired
    public EventService(IEventRepository eventRepository, EventMapper eventMapper) {
        this._eventRepository = eventRepository;
        this._eventMapper = eventMapper;
    }



    @Override
    public Page<EventDto> getAllEvents(String name, String city, String beforeThan, String afterThan, String type, Pageable pageable) {
        Page<Event> events = this._eventRepository.findEventsWithFilters(name, city, beforeThan, afterThan, type, pageable);

        return events.map(_eventMapper::entityToDto);
    }


    public Optional<Event> getEventByID(Long id) {
        return this._eventRepository.findById(id);
    }
    @Override
    public Optional<EventDto> findById(Long id) {
        Optional<Event> event =  this._eventRepository.findById(id);
        return event.map(this._eventMapper::entityToDto);
    }

    @Override
    public void updateEvent(Long id, EventDto eventDto) {
        Optional<Event> existingEvent = _eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setName(eventDto.name());
            event.setDescription(eventDto.description());
            event.setCity(eventDto.city());
            event.setLocation(eventDto.location());
            event.setType(eventDto.type());
            event.setDate(eventDto.date());
            event.setImage(eventDto.image());
            Event updatedEvent = _eventRepository.saveAndFlush(event);
            _eventMapper.entityToDto(updatedEvent);

        }
    }


    @Override
    public void deleteEvent(Long id) {
        _eventRepository.deleteById(id);

    }

    @Override
    public EventDto createEvent(EventDto eventDto) {
        Event event = _eventMapper.dtoToEntity(eventDto);
        Event createdEvent = _eventRepository.save(event);
        return _eventMapper.entityToDto(createdEvent);

    }

//    public EventDto toggleFavourite(Long eventID, boolean isFavourite) {
//        try {
//            Optional<Event> optionalEvent = this._eventRepository.findById(eventID);
//
//            if (optionalEvent.isPresent()) {
//                Event event = optionalEvent.get();
//                event.setFavourite(isFavourite);
//                Event updatedEvent = this._eventRepository.save(event);
//
//                return _eventMapper.entityToDto(updatedEvent);
//            } else {
//                log.warn("No event found with ID {} to update favourite status.", eventID);
//                throw new EntityNotFoundException("No event found with the specified ID.");
//            }
//        } catch (Exception ex) {
//            log.error("An unexpected error occurred while updating favourite status for event with ID {}.", eventID, ex);
//            throw new RuntimeException("Failed to toggle favourite status for the event.", ex);
//        }
//    }

}
