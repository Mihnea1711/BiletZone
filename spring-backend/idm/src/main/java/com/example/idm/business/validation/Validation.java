package com.example.idm.business.validation;

import com.example.idm.models.User;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        // Additional validation for specific fields, if needed
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateRole(user.getRole());
    }

    public static void validateCredentials(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        validateEmail(email);
        validatePassword(password);
    }

    private static void validateEmail(String email) {
        // Add specific validation logic for the username, if needed
        if (email.length() < 3 || email.length() > 50) {
            throw new IllegalArgumentException("Email must be between 3 and 50 characters");
        }

        // Validate email format using regex
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Additional username validation rules can be added here
    }

    private static void validatePassword(String password) {
        // Add specific validation logic for the password, if needed
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        // Additional password validation rules can be added here
    }

    private static void validateRole(String role) {
        // Add specific validation logic for the role, if needed
        if (!role.toLowerCase(Locale.ROOT).equals("admin") && !role.toLowerCase(Locale.ROOT).equals("user")) {
            throw new IllegalArgumentException("Invalid role. Supported roles: admin, user");
        }
        // Additional role validation rules can be added here
    }
}
