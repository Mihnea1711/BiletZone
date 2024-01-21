package com.example.database.persistence.interfaces;

import com.example.database.models.Event;
import com.example.database.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEventId_Id(Long eventId);
}
