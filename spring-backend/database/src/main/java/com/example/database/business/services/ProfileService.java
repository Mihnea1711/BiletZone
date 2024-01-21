package com.example.database.business.services;

import com.example.database.business.interfaces.IEventService;
import com.example.database.business.interfaces.IProfileService;
import com.example.database.business.validation.ProfileValidation;
import com.example.database.dtos.EventDto;
import com.example.database.dtos.HallMapDto;
import com.example.database.dtos.ProfileDto;
import com.example.database.dtos.PurchasedTicketDto;
import com.example.database.models.*;
import com.example.database.persistence.interfaces.IEventRepository;
import com.example.database.persistence.interfaces.IProfileRepository;
import com.example.database.persistence.interfaces.IPurchasedTicketRepository;
import com.example.database.persistence.interfaces.ITicketRepository;
import com.example.database.persistence.mappers.EventMapper;
import com.example.database.persistence.mappers.ProfileMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfileService implements IProfileService {

    private final EventMapper eventMapper;
    private final IProfileRepository _profileRepository;
    private final IEventRepository _eventRepository;
    private final ProfileMapper _profileMapper;

    private final IPurchasedTicketRepository _purchasedTicketRepository;
    private final ITicketRepository _ticketRepository;
    @Autowired
    public ProfileService(IProfileRepository profileRepository, IEventRepository eventRepository, ProfileMapper profileMapper, EventMapper eventMapper, IPurchasedTicketRepository purchasedTicketRepository, ITicketRepository ticketRepository) {
        this._profileRepository = profileRepository;
        this._eventRepository = eventRepository;
        this._profileMapper = profileMapper;
        this.eventMapper = eventMapper;
        this._purchasedTicketRepository=purchasedTicketRepository;
        this._ticketRepository=ticketRepository;
    }

    @Override
    public List<Profile> getAllProfiles() {
        return this._profileRepository.findAll();
    }

    @Override
    public Optional<ProfileDto> getProfileByID(Long id) {
        // Use the repository to find a profile by ID
        Optional<Profile> profileOptional = _profileRepository.findById(id);
        return profileOptional.map(_profileMapper::entityToDto);
    }

    @Override
    public Optional<ProfileDto> getProfileByUserUUID(String userUUID) {
        ProfileValidation.validateUserUUID(userUUID);

        Optional<Profile> profile =  this._profileRepository.findByUserUUID(userUUID);

        return profile.map(this._profileMapper::entityToDto);
    }



    @Override
    public void createProfile(ProfileDto profileDto) {
        Profile profile = this._profileMapper.dtoToEntity(profileDto);

        // Validate the profile data
        ProfileValidation.validateProfile(profile);

        // Save the profile
        Profile profileToADD = new Profile(profile.getFirstName(), profile.getLastName(), profile.getPhoneNumber(), profile.getUserUUID());

        this._profileRepository.save(profileToADD);

        // Log user creation
        log.info("Profile {} created.", profileToADD.getId());
    }

    @Override
    public void updateProfile(String uuid, ProfileDto profileDto) {

        // Check if the profile with the given ID exists
        System.out.println("Inainte de find by UUID");
        Optional<Profile> existingProfileOptional = _profileRepository.findByUserUUID(uuid);
        System.out.println("Dupa find by UUID");
        if (existingProfileOptional.isPresent()) {
            //Retrieve the existing profile
            System.out.println("Luam existing profile");
            Profile existingProfile = existingProfileOptional.get();
            System.out.println(profileDto);
            Profile newProfile=_profileMapper.dtoToEntity(profileDto);
            System.out.println(newProfile);
            // Validate the profile
            //ProfileValidation.validateProfile(newProfile);

            // Update the fields of the existing profile with the new data
            existingProfile.setFirstName(newProfile.getFirstName());
            existingProfile.setLastName(newProfile.getLastName());
            existingProfile.setPhoneNumber(newProfile.getPhoneNumber());


            // Save the updated profile
            _profileRepository.save(existingProfile);

            // Log the profile update
            log.info("Profile {} updated.", existingProfile.getId());
        }
        else
        {
            // Log an error if the profile with the given ID is not found
            log.error("Profile with UUID {} not found for updating.", uuid);
        }
    }

    @Override
    public void deleteProfile(Long id) {
        if (_profileRepository.existsById(id)) {
            _profileRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }
    }

    @Override
    public List<PurchasedTicketDto> getPurchasedTickets(String userUUID) {
        System.out.println("Metoda getPurchasedTickets a fost apelată pentru userUUID: " + userUUID);
        List<PurchasedTicket> purchasedTickets = this._purchasedTicketRepository.findByUserUUID(userUUID);
        System.out.println("Am gasit "+purchasedTickets.get(0).getUserUUID());
        return purchasedTickets.stream()
                .map(purchasedTicket -> {
                    Ticket ticket = purchasedTicket.getTicket();  // modificarea aici

                    // Apelezi metoda pentru a obține informații despre eveniment
                    Event event = ticket.getEvent();

                    EventDto eventDto = mapEventToEventDto(event);

                    return new PurchasedTicketDto(
                            purchasedTicket.getPurchaseId(),
                            purchasedTicket.getTicket().getId(),
                            purchasedTicket.getUserUUID(),
                            purchasedTicket.getQuantity(),
                            eventDto
                    );
                })
                .collect(Collectors.toList());
    }

    public EventDto mapEventToEventDto(Event event) {
        return new EventDto(
                event.getId() != null ? event.getId().longValue() : null,
                event.getName(),
                event.getDescription(),
                event.getCity(),
                event.getLocation(),
                event.getType(),
                event.getDate(),
                event.getImage()
        );
    }

    public HallMapDto mapHallMapToHallMapDto(HallMap hallMap) {
        return new HallMapDto(
                hallMap.getId(),
                hallMap.getNumRows(),
                hallMap.getNumColumns()
        );
    }


    @Override
    public List<EventDto> getFavoriteEvents(String userUUID) {
        List<Event> events = this._profileRepository.findFavoriteEventsByUserUUID(userUUID);
        return events.stream().map(eventMapper::entityToDto).collect(Collectors.toList());
    }



    @Override
    public void addEventToFavorites(String userUUID, Long eventId) throws Exception {
        Profile profile = this._profileRepository.findByUserUUID(userUUID)
                .orElseThrow(() -> new Exception("Profile not found with userUUID: " + userUUID));

        Event event = this._eventRepository.findById(eventId)
                .orElseThrow(() -> new Exception("Event not found with id: " + eventId));

        Set<Event> favoriteEvents = new HashSet<>(profile.getFavoriteEvents());
        favoriteEvents.add(event);
        profile.setFavoriteEvents(favoriteEvents);

        this._profileRepository.save(profile);
    }

    @Override
    public void removeEventFromFavorites(String userUUID, Long eventId) throws Exception {
        Profile profile = this._profileRepository.findByUserUUID(userUUID)
                .orElseThrow(() -> new Exception("Profile not found with userUUID: " + userUUID));

        Event event = this._eventRepository.findById(eventId)
                .orElseThrow(() -> new Exception("Event not found with id: " + eventId));

        Set<Event> favoriteEvents = profile.getFavoriteEvents();
        favoriteEvents.remove(event);

        this._profileRepository.save(profile);
    }
}
