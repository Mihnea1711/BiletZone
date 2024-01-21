package com.example.gateway.utils;

import io.micrometer.observation.Observation;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Endpoints {

    // ANGULAR ENDPOINTS
    public static final String CONFIRMATION_MAIL_ANGULAR_ENDPOINT = "/confirm-email";

    // GATEWAY ENDPOINTS
    public static final String GATEWAY_PREFIX = "/api";

    public static final String REGISTER_ENDPOINT = "/register";
    public static final String LOGIN_ENDPOINT = "/login";

    public static final String SEND_CONFIRMATION_MAIL = "/send-confirmation";
    public static final String SEND_REGULAR_MAIL = "/send-mail";
    public static final String CONFIRM_EMAIL = "/confirm-email";

    public static final String GET_EVENTS = "/events";
    public static final String ADD_EVENT_TO_FAVORITES = "/events/{eventID}/favorites";
    public static final String DELETE_EVENT_FROM_FAVORITES = "/events/{eventID}/favorites";

    public static final String GET_FAVORITE_EVENTS = "/profile/favorites";

    public static final String GET_PROFILE="/profile";
    public static final String EDIT_PROFILE="/profiles";

    public static final String GET_PURCHASED_TICKETS="/profiles/tickets";



    // MAIL ENDPOINTS
    public static final String MAIL_SEND_CONFIRMATION = "/mail/send-confirmation";
    public static final String MAIL_SEND_REGULAR = "/mail/send-mail";


    // IDM ENDPOINTS
    public static final String IDM_REGISTER_ENDPOINT = "/idm/users";
    public static final String IDM_LOGIN_ENDPOINT = "/idm/login";
    public static final String IDM_GET_USER_BY_UUID_ENDPOINT = "/idm/users/";
    public static final String IDM_GET_USER_BY_EMAIL_ENDPOINT = "/idm/users/email/";
    public static final String IDM_CONFIRM_MAIL_ENDPOINT = "/idm/confirm-mail";
    public static final String IDM_GET_USERS_FOR_NOTIFICATIONS = "/idm/notified-users";

    // PROFILE ENDPOINTS
    public static final String DATABASE_CREATE_PROFILE_ENDPOINT = "/main/profiles";
    public static final Function<String, String> DATABASE_GET_FAVORITE_EVENTS = userUUID -> "/main/users/" + userUUID + "/favorite-events";
    public static final BiFunction<String, String, String> DATABASE_ADD_FAVORITE_EVENTS = (userUUID, eventId) -> "/main/users/" + userUUID + "/favorite-events/" + eventId;
    public static final BiFunction<String, String, String> DATABASE_DELETE_FAVORITE_EVENTS = (userUUID, eventId) -> "/main/users/" + userUUID + "/favorite-events/" + eventId;
    public static final Function<String, String> DATABASE_GET_PROFILE = userUUID ->  "/main/profiles/" + userUUID;
    public static final Function<String, String> DATABASE_EDIT_PROFILE = uuid -> "/main/profiles/"+uuid;
    public static final Function<String, String> DATABASE_GET_PURCHASED_TICKETS = userUUID ->  "/main/profile/" + userUUID +"/tickets";


    // EVENT ENDPOINTS
    public static final String DATABASE_GET_ALL_EVENTS = "/main/events";
    public static final   Function<String, String> DATABASE_DELETE_EVENT = str ->"/main/events/" + str;
    public static Function<String, String> DATABASE_TOGGLE_FAVOURITE = str -> "/main/events/" + str;

    public static final String DATABASE_ADD_MESSAGE = "/messages/";

    public static final String GET_ALL_MESSAGES = "/messages/events/";

    public static final String UNIVERSAL_MESSAGE = "/messages/";

    public static final String ADD_USER = "/add-user";

    public static final String DB_GET_ALL_PROFILES = "/main/profiles";

    public static final String GET_ALL_PROFILES = "/profiles";

    public static Function<String, String> DATABASE_DELETE_PROFILE = str -> "/main/profiles/" + str;

    public static final String DELETE_PROFILE = "/profiles/{id}";
    public static final String DELETE_EVENT = "/events/{id}";

    public static Function<String, String> DATABASE_DELETE_USER = str -> "/idm/users/" + str;

    public static final String DELETE_USER = "/users/";


}
