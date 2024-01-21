package com.example.database.persistence.interfaces;

import com.example.database.models.Event;
import com.example.database.models.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserUUID(String userUUID);

//    @Query("SELECT p.favoriteEvents FROM Profile p WHERE p.userUUID = :userUUID")
//    List<Event> findFavoriteEventsByUserUUID(@Param("userUUID") String userUUID);
    @Query("SELECT DISTINCT p.favoriteEvents FROM Profile p WHERE p.userUUID = :userUUID")
    List<Event> findFavoriteEventsByUserUUID(@Param("userUUID") String userUUID);


    // Add the following method
    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.favoriteEvents = :favoriteEvents WHERE p.userUUID = :userUUID")
    void updateEventToFavorites(
            @Param("userUUID") String userUUID,
            @Param("favoriteEvents") Set<Event> favoriteEvents
    );
}
