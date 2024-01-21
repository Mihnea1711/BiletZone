package com.example.idm.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_uuid")
    private String uuid;

    @Basic
    @Column(name = "email", unique = true)
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Basic
    @Column(name = "password")
    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Basic
    @Column(name = "role")
    @NotNull(message = "Role cannot be null")
    @NotEmpty(message = "Role cannot be empty")
    private String role;

    @Basic
    @Column(name = "confirmation_token")
    @NotNull(message = "ConfirmationToken cannot be null")
    private String confirmationToken;

    @Basic
    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @Basic
    @Column(name = "is_notifications_enabled")
    private boolean isNotificationsEnabled;

    // Parameterized constructor
    public User(String email, String password, String role, String confirmationToken, boolean isConfirmed, boolean isNotificationsEnabled) {
        this.uuid = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.role = role;
        this.confirmationToken = confirmationToken;
        this.isConfirmed = isConfirmed;
        this.isNotificationsEnabled = isNotificationsEnabled;
    }
}
