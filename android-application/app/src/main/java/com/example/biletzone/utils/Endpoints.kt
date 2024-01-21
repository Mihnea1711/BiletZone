package com.example.biletzone.utils

object Endpoints {
    const val GATEWAY_REGISTER = "/api/register"
    const val GATEWAY_LOGIN = "/api/login"

    const val GATEWAY_GET_EVENTS = "/api/events"
    const val GATEWAY_ADD_EVENT = "/api/events"
    val GATEWAY_GET_EVENT_BY_ID: (String) -> String = { eventID -> "/api/events/${eventID}" }

    const val GATEWAY_GET_FAVORITE_EVENTS = "/api/profile/favorites"
    val GATEWAY_ADD_EVENT_TO_FAVORITES: (String) -> String = { eventID -> "/api/events/$eventID/favorites" }
    val GATEWAY_DELETE_EVENT_FROM_FAVORITES: (String) -> String = { eventID -> "/api/events/$eventID/favorites" }

    const val GATEWAY_CONFIRMATION_MAIL = "/api/confirm-email"

    const val GATEWAY_GET_PROFILE = "/api/profile"
}