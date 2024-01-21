package com.example.database.utils;

public final class Endpoints {
    public static final String HEALTH_ENDPOINT = "/health";
    public static final String CREATE_ROUTE = "/profiles";
    public static final String UPDATE_ROUTE = "/profiles";
    public static final String GET_PROFILE_ROUTE="/profiles";
    public static final String GET_FAVORITE_EVENTS = "/users/{userUUID}/favorite-events";
    public static final String ADD_FAVORITE_EVENTS = "/users/{userUUID}/favorite-events/{eventId}";
    public static final String DELETE_FAVORITE_EVENTS = "/users/{userUUID}/favorite-events/{eventId}";

    public static final String GET_ALL_PROFILES = "/profiles";

    public static final String DELETE_PROFILE = "/profiles/{id}";

    public static final String GET_PURCHASED_TICKETS="/profile/{userUUID}/tickets";
    public static final String DELETE_EVENT = "/events/{id}" ;
}
