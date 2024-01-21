package com.example.database.business.validation;

import com.example.database.models.Profile;

import java.util.regex.Pattern;

public class ProfileValidation {

    private static final String FIRST_NAME_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$";
    private static final String LAST_NAME_REGEX = "^[a-zA-Z]+$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";
    private static final String UUID_REGEX = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    public static void validateProfile(Profile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        // Additional validation for specific fields, if needed
        validateFirstName(profile.getFirstName());
        validateLastName(profile.getLastName());
        validatePhoneNumber(profile.getPhoneNumber());
        validateUserUUID(profile.getUserUUID());
    }

    public static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First Name cannot be null or empty");
        }

        if (!Pattern.matches(FIRST_NAME_REGEX, firstName)) {
            throw new IllegalArgumentException("Invalid First Name format");
        }
    }

    public static void validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last Name cannot be null or empty");
        }

        if (!Pattern.matches(LAST_NAME_REGEX, lastName)) {
            throw new IllegalArgumentException("Invalid Last Name format");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone Number cannot be null or empty");
        }

        if (!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
            throw new IllegalArgumentException("Invalid Phone Number format");
        }
    }

    public static void validateUserUUID(String userUUID) {
        if (userUUID == null || userUUID.isEmpty()) {
            throw new IllegalArgumentException("User UUID reference cannot be null or empty");
        }

        if (!Pattern.matches(UUID_REGEX, userUUID)) {
            throw new IllegalArgumentException("Invalid User UUID format");
        }
    }
}
