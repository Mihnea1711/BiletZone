package com.example.idm.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Cryptography {
    // Create an instance of BCryptPasswordEncoder
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Hash the password
    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Check if the entered password matches the hashed password
    public static boolean checkPassword(String enteredPassword, String hashedPassword) {
        return passwordEncoder.matches(enteredPassword, hashedPassword);
    }
}
