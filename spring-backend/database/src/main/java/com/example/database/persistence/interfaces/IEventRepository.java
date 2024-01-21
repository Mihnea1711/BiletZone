package com.example.database.persistence.interfaces;

import com.example.database.models.Event;
import com.example.database.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:city IS NULL OR e.city = :city) " +
            "AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (:beforeThan IS NULL OR STR_TO_DATE(:beforeThan, '%Y-%m-%d') <= STR_TO_DATE(e.date, '%Y-%m-%d')) " +
            "AND (:afterThan IS NULL OR STR_TO_DATE(:afterThan, '%Y-%m-%d') >= STR_TO_DATE(e.date, '%Y-%m-%d'))"
    )
    Page<Event> findEventsWithFilters(
            @Param("name") String name,
            @Param("city") String city,
            @Param("beforeThan") String beforeThan,
            @Param("afterThan") String afterThan,
            @Param("eventType") String eventType,
            Pageable pageable
    );

    Optional<Event> findById(Long eventId);
}