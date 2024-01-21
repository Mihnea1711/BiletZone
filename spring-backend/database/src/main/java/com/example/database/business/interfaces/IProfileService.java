package com.example.database.business.interfaces;

import com.example.database.dtos.EventDto;
import com.example.database.dtos.ProfileDto;
import com.example.database.dtos.PurchasedTicketDto;
import com.example.database.models.Profile;

import java.util.List;
import java.util.Optional;

public interface IProfileService {
    void createProfile(ProfileDto profileDto);

    List<Profile> getAllProfiles();

    Optional<ProfileDto> getProfileByID(Long id);

    Optional<ProfileDto> getProfileByUserUUID(String userUUID);

    void updateProfile(String uuid, ProfileDto profile);

    void deleteProfile(Long id);

    List<EventDto> getFavoriteEvents(String userUUID);
    void addEventToFavorites(String userUUID, Long eventId) throws Exception;
    void removeEventFromFavorites(String userUUID, Long eventId) throws Exception;
    public List<PurchasedTicketDto> getPurchasedTickets(String userUUID);
}
