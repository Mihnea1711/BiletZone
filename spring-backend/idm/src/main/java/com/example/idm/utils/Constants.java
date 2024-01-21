package com.example.idm.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class Constants {
    // Other Constants
    public static final String JWT_SECRET_STRING = "EqjWQ7MYqS4VkF2vZLNOOH9r4XVa3XrIsibni4cJ6eEsAphTKHvnMoRmZdfC0PLD";

    public static final String IDM_PREFIX = "/idm";

    // Private constructor to prevent instantiation
    private Constants() {}
}
