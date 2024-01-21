package com.example.biletzone.utils

import com.example.biletzone.models.Event
import com.example.biletzone.models.Profile

object Constants {
    const val USER_ROLE = "user"
    const val ADMIN_ROLE = "admin"

    const val GATEWAY_SCHEMA = "http://"
    const val GATEWAY_HOST = "192.168.100.13"
    const val GATEWAY_PORT = 8080

    const val JWT_SHARED_PREF_NAME = "jwt"
    const val SHARED_PREFERENCES_NAME = "MyPreferences"

    val eventList: ArrayList<Event> = arrayListOf(
        Event(
            id = 1,
            name = "Concert",
            description = "Live music performance",
            city = "New York",
            location = "Madison Square Garden",
            type = "Music",
            date = "2024-02-01",
            image = "https://images.unsplash.com/photo-1704212224803-42e34f022c36?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDh8RnpvM3p1T0hONnd8fGVufDB8fHx8fA%3D%3D"
        ),
        Event(
            id = 2,
            name = "Tech Conference",
            description = "Explore the latest in technology",
            city = "San Francisco",
            location = "Moscone Center",
            type = "Technology",
            date = "2024-03-15",
            image = "https://images.unsplash.com/photo-1705281238460-28fe48836205?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ),
        Event(
            id = 3,
            name = "Art Exhibition",
            description = "Contemporary art showcase",
            city = "Paris",
            location = "Louvre Museum",
            type = "Art",
            date = "2024-04-20",
            image = "https://images.unsplash.com/photo-1633627243409-740c62651c5d?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDF8fHxlbnwwfHx8fHw%3D"
        )
    )

    val userProfile = Profile(
        id = 1,
        firstName = "John",
        lastName = "Doe",
        phoneNumber = "1234567890",
        userUUID = "abcd-1234-efgh-5678"
    )

    const val PROFILE_KEY = "PROFILE_KEY"
    const val NEW_EVENT_KEY = "NEW_EVENT_DATA"
}