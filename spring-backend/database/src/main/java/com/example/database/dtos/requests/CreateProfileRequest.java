package com.example.database.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileRequest {
    @NotNull(message = "First Name cannot be null")
    @NotEmpty(message = "First Name cannot be empty")
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;

    @NotNull(message = "Phone Number cannot be null")
    @NotEmpty(message = "Phone Number cannot be empty")
    private String phoneNumber;

    @NotNull(message = "UserUUID cannot be null")
    @NotEmpty(message = "UserUUID cannot be empty")
    private String userUUID;
}
