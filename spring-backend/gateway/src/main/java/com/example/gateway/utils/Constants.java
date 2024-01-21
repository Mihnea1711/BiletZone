package com.example.gateway.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Constants {
    public static final String ANGULAR_HOST = "localhost";
    public static final int ANGULAR_PORT = 4200;

    public static final String JWT_SECRET_STRING = "EqjWQ7MYqS4VkF2vZLNOOH9r4XVa3XrIsibni4cJ6eEsAphTKHvnMoRmZdfC0PLD";

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 50;
    public static final int DEFAULT_PAGE = 0;

    public static final String ROLE_CLAIM = "role";
    public static final String SUB_CLAIM = "sub";
    public static final String ADMIN_ROLE = "admin";
    public static final String USER_ROLE = "user";

    public static final List<String> EXCLUDED_ENDPOINTS = Arrays.asList(
            Endpoints.GATEWAY_PREFIX + Endpoints.REGISTER_ENDPOINT,
            Endpoints.GATEWAY_PREFIX + Endpoints.LOGIN_ENDPOINT,
            Endpoints.GATEWAY_PREFIX + Endpoints.GET_EVENTS,
            Endpoints.GATEWAY_PREFIX + Endpoints.CONFIRM_EMAIL,
            Endpoints.GATEWAY_PREFIX + Endpoints.GET_FAVORITE_EVENTS,
            Endpoints.GATEWAY_PREFIX + Endpoints.GET_PROFILE,
            Endpoints.GATEWAY_PREFIX + Endpoints.DATABASE_ADD_MESSAGE,
            Endpoints.GATEWAY_PREFIX + Endpoints.GET_ALL_MESSAGES,
            Endpoints.GATEWAY_PREFIX + Endpoints.UNIVERSAL_MESSAGE,
            Endpoints.GATEWAY_PREFIX + Endpoints.ADD_USER,
            Endpoints.GATEWAY_PREFIX + Endpoints.DELETE_PROFILE,
            Endpoints.GATEWAY_PREFIX + Endpoints.GET_ALL_PROFILES,
            Endpoints.GATEWAY_PREFIX + Endpoints.DELETE_USER,
            Endpoints.GATEWAY_PREFIX + Endpoints.EDIT_PROFILE
    );

    public static final String ADMIN_REGISTRATION_SUBJECT = "Welcome to [BiletZone] - Admin Registration Confirmation";
    public static final String UPCOMING_EVENTS_NOTIFICATION = "Your Upcoming Favorite Events: Exciting Plans Await You!";

    public static String TOKEN_QUERY_PARAM = "token";

    // Private constructor to prevent instantiation
    private Constants() {
        // Prevent instantiation of this class
    }
}
