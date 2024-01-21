package com.example.database.persistence.interfaces;

import com.example.database.models.Event;
import com.example.database.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.event.id = :eventId")
    List<Ticket> findByIdEvent(Long eventId);

    // Găsește un bilet după ticketId
    Optional<Ticket> findById(Long ticketId);
}