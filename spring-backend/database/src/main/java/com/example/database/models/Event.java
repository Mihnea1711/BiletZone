package com.example.database.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Basic
    @Column(name = "name")
    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Basic
    @Column(name = "description")
    @NotNull(message = "Description cannot be null")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Basic
    @Column(name = "city")
    @NotNull(message = "City cannot be null")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @Basic
    @Column(name = "location")
    @NotNull(message = "Location cannot be null")
    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @Basic
    @Column(name = "type")
    @NotNull(message = "Event Type cannot be null")
    @NotEmpty(message = "Event Type cannot be empty")
    private String type;

    @Basic
    @Column(name = "date")
    @NotNull(message = "Date cannot be null")
    @NotEmpty(message = "Date cannot be empty")
    private String date;

    @Basic
    @Column(name = "image")
    @NotNull(message = "Image cannot be null")
    @NotEmpty(message = "Image cannot be empty")
    private String image;


    @ManyToMany(mappedBy = "favoriteEvents", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Profile> favoritedByProfiles;

    public Event(String name, String description, String location, String date, String image) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.image = image;
        this.favoritedByProfiles = new HashSet<>();
    }
}
