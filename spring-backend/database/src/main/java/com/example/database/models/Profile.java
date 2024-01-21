package com.example.database.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "profile", uniqueConstraints = @UniqueConstraint(columnNames = "user_uuid"))
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Basic
    @Column(name = "first_name")
    @NotNull(message = "First Name cannot be null")
    @NotEmpty(message = "First Name cannot be empty")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    @NotNull(message = "Last Name cannot be null")
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;

    @Basic
    @Column(name = "phone_number")
    @NotNull(message = "Phone Number cannot be null")
    @NotEmpty(message = "Phone Number cannot be empty")
    private String phoneNumber;

    @Basic
    @Column(name = "user_uuid", unique = true)
    @NotNull(message = "UserUUID cannot be null")
    @NotEmpty(message = "UserUUID cannot be empty")
    private String userUUID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "profile_event_favorite",
            joinColumns = {
                    @JoinColumn(name = "user_uuid", referencedColumnName = "user_uuid")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
            }
    )
    @JsonManagedReference
    private Set<Event> favoriteEvents;

    // Parameterized constructor (if needed)
    public Profile(String firstName, String lastName, String phoneNumber, String userUUID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userUUID = userUUID;
        this.favoriteEvents = new HashSet<>();
    }
}
