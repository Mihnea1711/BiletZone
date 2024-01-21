package com.example.database.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event eventId;

    @Basic
    @Column(name = "user_uuid")
    @NotNull(message = "UserUUID cannot be null")
    @NotEmpty(message = "UserUUID cannot be empty")
    private String userUUID;

    @Basic
    @Column(name = "message_text", columnDefinition = "TEXT")
    @NotNull(message = "Text cannot be null")
    @NotEmpty(message = "Text cannot be empty")
    private String messageText;

    public Message(Event eventId, String userUUID, String messageText) {
        this.eventId = eventId;
        this.userUUID = userUUID;
        this.messageText = messageText;
    }
}
